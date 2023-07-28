package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.*;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.CommonUtils;
import com.panghu.housemanage.common.util.DateTimeUtils;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.dao.LeaseMapper;
import com.panghu.housemanage.dao.LeaseMemberRelMapper;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.LeaseMemberRelPo;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.LeaseService;
import com.panghu.housemanage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaseServiceImpl implements LeaseService {
    @Autowired
    LeaseMapper leaseMapper;
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    MemberService memberService;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    LeaseMemberRelMapper leaseMemberRelMapper;

    @Override
    public List<LeaseVo> queryLeaseByMemberId(Long memberId) {
        List<LeaseVo> leaseVos = leaseMapper.queryLeaseByMemberId(memberId);
        leaseVos.forEach(vo -> vo.setVoUnit(vo.getUnit()+ vo.getLeaseType().getUnit()));
        return leaseVos;
    }

    @Override
    public IPage<LeaseVo> pageQueryLease(Page<LeaseVo> page, LeaseVo leaseVo) {
        preprocess(leaseVo);
        Page<LeaseVo> leaseVoPage = leaseMapper.pageQueryLease(page, leaseVo);
        leaseVoPage.getRecords().forEach(vo -> vo.setVoUnit(vo.getUnit()+vo.getLeaseType().getUnit()));
        return leaseVoPage;
    }

    private void preprocess(LeaseVo vo) {
        String memberName = vo.getMemberName();
        if (memberName != null && (memberName.contains(",") || memberName.contains("，"))) {
            memberName = memberName.replace("，", ",");
            vo.setMemberNames(Arrays.asList(memberName.split(",")));
            vo.setMemberName(null);
        }
        String roomNo = vo.getRoomNo();
        if (roomNo != null && (roomNo.contains(",") || roomNo.contains("，"))) {
            roomNo = roomNo.replace("，", ",");
            vo.setRoomNos(Arrays.asList(roomNo.split(",")));
            vo.setRoomNo(null);
        }
    }

    @Override
    @Transactional
    public void insertLease(LeaseVo leaseVo) {
        // 验重
        List<LeaseVo> voList = checkUnique(leaseVo);
        if (!CollectionUtils.isEmpty(voList)) {
            String detail = generateFormattedString(voList);
            throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_LEASE, detail);
        }
        // 判断是否需要创建租客记录
        List<MemberVo> members = leaseVo.getMembers();
        for (MemberVo member: members
             ) {
            if (ObjectUtils.isEmpty(member.getRowId())) {
                MemberPo memberPo = MemberPo.builder().name(member.getMemberName()).tel(member.getTel()).sex(member.getSex().getCode()).idCard(member.getIdCard()).status(1).build();
                // 验重
                MemberPo exist = memberService.checkUnique(memberPo);
                if (exist == null) {
                    memberMapper.insert(memberPo);
                    member.setRowId(memberPo.getId());
                }else {
                    if (exist.getSex() == member.getSex().getCode() && exist.getTel().equals(member.getTel()) || exist.getName().equals(member.getMemberName())) {
                        member.setRowId(exist.getId());
                    }else {
                        // 和系统内数据对不上
                        throw new PHServiceException(PHExceptionCodeEnum.ERROR_MEMBER_INFO, exist.getName()+"-"+ MemberSexEnum.getValueByCode(exist.getSex())+"-"+exist.getIdCard()+"-"+exist.getTel());
                    }
                }
            }
        }

        // 数据模型转换
        LeasePo leasePo = RequestHandleUtils.<LeaseVo, LeasePo> modelDTOTrans(Collections.singletonList(leaseVo)).get(0);
        // 生成业务单号
        String leaseNumber = genLeaseNumber(leaseVo);
        leasePo.setNumber(leaseNumber);
        // 检查是否需要更新租客和房间状态
        updateStatusForInsert(members, leasePo.getRoomId());
        // 新增租约记录
        leaseMapper.insert(leasePo);
        members.forEach(member -> {
            LeaseMemberRelPo po = LeaseMemberRelPo.builder().leaseId(leasePo.getId()).memberId(member.getRowId()).build();
            leaseMemberRelMapper.insert(po);
        });
    }


    public String generateFormattedString(List<LeaseVo> voList) {
        StringBuilder result = new StringBuilder();
        for (LeaseVo leaseVo : voList) {
            String line = leaseVo.getLeaseNumber() + "：" + leaseVo.getRoomNo() + "房 - " + leaseVo.getMemberName() + "\n";
            result.append(line);
        }
        return result.toString();
    }



    private void updateStatusForInsert(List<MemberVo> members, Long roomId) {
        for (MemberVo vo: members
             ) {
            Long memberId = vo.getRowId();
            List<LeaseVo> voList = leaseMapper.queryLeaseByMemberId(memberId);
            List<LeaseVo> list = voList.stream().filter(leaseVo -> leaseVo.getEffective() == EffectiveEnum.EFFECTIVE).toList();
            if (CollectionUtils.isEmpty(list)) {
                // 不存在活跃租约，需要更改状态
                MemberPo memberPo = MemberPo.builder().id(memberId).status(MemberStatusEnum.RENTING.getCode()).build();
                memberMapper.updateById(memberPo);
            }
        }

        List<LeaseVo> voList = leaseMapper.queryLeaseByRoomId(roomId);
        List<LeaseVo> list = voList.stream().filter(leaseVo -> leaseVo.getEffective() == EffectiveEnum.EFFECTIVE).toList();
        if (CollectionUtils.isEmpty(list)) {
            // 不存在活跃租约，需要更改状态
            RoomPo roomPo = RoomPo.builder().id(roomId).status(RoomStatusEnum.INUSE.getCode()).build();
            roomMapper.updateById(roomPo);
        }
    }

    @Override
    @Transactional
    public void updateBatch(List<LeaseVo> voList) {
        List<LeasePo> poList = RequestHandleUtils.modelDTOTrans(voList);
        if ("edit".equals(voList.get(0).getOptType())) {
            // 租约修改将失效原租约，创建新租约
            edit(voList.get(0));
        }else {
            terminate(poList);
        }
    }

    private void edit(LeaseVo leaseVo) {
        List<LeasePo> leasePos = RequestHandleUtils.modelDTOTrans(List.of(leaseVo));
        // 作废原租约
        LeasePo leasePo = leasePos.get(0);
        LeasePo build = LeasePo.builder().id(leasePo.getId()).effective(EffectiveEnum.UN_EFFECTIVE.getCode()).build();
        // 原房间的所有租约
        Long oldRoomId = leaseMapper.selectById(leasePo.getId()).getRoomId();
        if (!Objects.equals(oldRoomId, leasePo.getRoomId())) {
            QueryWrapper<LeasePo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_id", oldRoomId).eq("effective", 1).ne("isdelete", 1);
            List<LeasePo> leaseList = leaseMapper.selectList(queryWrapper);
            // 当前生效租约，失效掉本次数量，如果清零了，就需要更新房间状态
            if (!CollectionUtils.isEmpty(leaseList) && leaseList.size() == 1) {
                LambdaUpdateWrapper<RoomPo> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(RoomPo::getStatus, 0).eq(RoomPo::getId, oldRoomId);
                roomMapper.update(null, updateWrapper);
            }
        }
        leaseMapper.updateById(build);
        // 创建新租约
        leaseVo.setRowId(null);
        insertLease(leaseVo);
    }

    private void terminate(List<LeasePo> poList){
        if (poList.get(0).getEffective() != null && poList.get(0).getEffective() == 0) {
            poList.forEach(leasePo -> leaseMapper.updateById(leasePo));
            // 只有失效场景才会使effective为0发起put请求
            updateStatusForTerminate(poList);
        }
    }

    /**
     * 更新租客状态
     *
     * @param poList 订单列表
     */
    private void updateStatusForTerminate(List<LeasePo> poList) {
        for (LeasePo po: poList
             ) {
            List<MemberVo> rentMemberByLeaseId = memberMapper.getRentMemberByLeaseId(po.getId());
            Map<Long, List<MemberVo>> collect = rentMemberByLeaseId.stream().collect(Collectors.groupingBy(MemberVo::getRowId));
            Map<Long, List<MemberVo>> filteredMap = collect.entrySet().stream()
                    .filter(entry -> entry.getValue().stream().noneMatch(vo -> vo.getLeaseEffective() == 1))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (Long memberId: filteredMap.keySet()
            ) {
                MemberPo memberPo = MemberPo.builder().id(memberId).status(MemberStatusEnum.SURRENDER.getCode()).build();
                memberMapper.updateById(memberPo);
            }

            List<LeaseVo> voList = leaseMapper.queryLeaseByRoomId(po.getRoomId());
            List<LeaseVo> list = voList.stream().filter(leaseVo -> leaseVo.getEffective() == EffectiveEnum.EFFECTIVE).toList();
            if (CollectionUtils.isEmpty(list)) {
                // 不存在活跃租约，需要更改状态
                RoomPo roomPo = RoomPo.builder().id(po.getRoomId()).status(RoomStatusEnum.UNUSED.getCode()).build();
                roomMapper.updateById(roomPo);
            }
        }
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<LeasePo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LeasePo::getIsDelete, 1).in(LeasePo::getId, ids);
        leaseMapper.update(null, updateWrapper);
    }

    @Override
    public List<LeaseVo> checkUnique(LeaseVo leaseVo) {
        List<Long> ids = leaseVo.getMembers().stream().map(MemberVo::getRowId).toList();
        Map<String, Object> params = new HashMap<>();
        params.put("memberIds", ids);
        params.put("roomId", leaseVo.getRoomId());
        return leaseMapper.checkUnique(params);
    }

    @Override
    public List<LeaseVo> queryLeaseByRoomId(Long roomId) {
        return leaseMapper.queryLeaseByRooomId(roomId);
    }

    private String genLeaseNumber(LeaseVo leaseVo) {
        char firstLetter = leaseVo.getLeaseType().name().charAt(0);
        String roomNo = leaseVo.getRoomNo();
        String initials = CommonUtils.getInitials(leaseVo.getMembers().get(0).getMemberName());
        String yyyyMMdd = DateTimeUtils.formatDate(leaseVo.getStartDate(), "yyyyMMdd");
        return String.join("-", String.valueOf(firstLetter)+roomNo, initials+yyyyMMdd);
    }
}
