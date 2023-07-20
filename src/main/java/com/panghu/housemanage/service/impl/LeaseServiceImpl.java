package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.EffectiveEnum;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.CommonUtils;
import com.panghu.housemanage.common.util.DateTimeUtils;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.dao.LeaseMapper;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
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
        LeasePo po = checkUnique(leaseVo);
        if (po != null) {
            throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_MEMBER, null);
        }
        // 判断是否需要创建租客记录
        if (ObjectUtils.isEmpty(leaseVo.getMemberId())) {
            MemberPo memberPo = MemberPo.builder().name(leaseVo.getMemberName()).tel(leaseVo.getTel()).sex(leaseVo.getSex()).idCard(leaseVo.getIdCard()).status(1).build();
            // 验重
            MemberPo member = memberService.checkUnique(memberPo);
            if (member == null) {
                memberMapper.insert(memberPo);
                leaseVo.setMemberId(memberPo.getId());
            }else {
                leaseVo.setMemberId(member.getId());
            }
        }
        // 数据模型转换
        LeasePo leasePo = RequestHandleUtils.<LeaseVo, LeasePo> modelDTOTrans(Collections.singletonList(leaseVo)).get(0);
        // 生成业务单号
        String leaseNumber = genLeaseNumber(leaseVo);
        leasePo.setNumber(leaseNumber);
        // 检查是否需要更新租客和房间状态
        updateStatusForInsert(leasePo);
        // 新增租约记录
        leaseMapper.insert(leasePo);
    }



    private void updateStatusForInsert(LeasePo leasePo) {
        Long memberId = leasePo.getMemberId();
        List<LeaseVo> voList = leaseMapper.queryLeaseByMemberId(memberId);
        List<LeaseVo> list = voList.stream().filter(leaseVo -> leaseVo.getEffective() == EffectiveEnum.EFFECTIVE).toList();
        if (CollectionUtils.isEmpty(list)) {
            // 不存在活跃租约，需要更改状态
            MemberPo memberPo = MemberPo.builder().id(memberId).status(MemberStatusEnum.RENTING.getCode()).build();
            memberMapper.updateById(memberPo);
        }

        Long roomId = leasePo.getRoomId();
        voList = leaseMapper.queryLeaseByRoomId(roomId);
        list = voList.stream().filter(leaseVo -> leaseVo.getEffective() == EffectiveEnum.EFFECTIVE).toList();
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
            // 只有失效场景才会使effective为0发起put请求
            updateStatusForTerminate(poList);
            poList.forEach(leasePo -> leaseMapper.updateById(leasePo));
        }
    }

    /**
     * 更新租客状态
     *
     * @param poList 订单列表
     */
    private void updateStatusForTerminate(List<LeasePo> poList) {
        // 租客分组
        Map<Long, Long> collect = poList.stream().collect(Collectors.groupingBy(LeasePo::getMemberId, Collectors.counting()));
        // 获取当前租客生效的租约
        Map<Long, Integer> memberLeaseMap = poList.stream()
                .map(LeasePo::getMemberId)
                .distinct()
                .collect(Collectors.toMap(
                        memberId -> memberId,
                        memberId -> leaseMapper.selectList(new QueryWrapper<LeasePo>().eq("member_id", memberId).eq("effective", 1).ne("isdelete", 1)).size()
                ));
        // 当前生效租约，失效掉本次数量，如果清零了，就需要更新租客状态
        List<Long> updateList = collect.entrySet().stream()
                .filter(entry -> memberLeaseMap.containsKey(entry.getKey()))
                .filter(entry -> memberLeaseMap.get(entry.getKey()) - entry.getValue() <= 0)
                .map(Map.Entry::getKey)
                .toList();

        if (!CollectionUtils.isEmpty(updateList)) {
            LambdaUpdateWrapper<MemberPo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(MemberPo::getStatus, 2).in(MemberPo::getId, updateList);
            memberMapper.update(null, updateWrapper);
        }

        // 房间分组
        Map<Long, Long> roomCollect = poList.stream().collect(Collectors.groupingBy(LeasePo::getRoomId, Collectors.counting()));
        // 获取当前房间生效的租约
        Map<Long, Integer> roomLeaseMap = poList.stream()
                .map(LeasePo::getRoomId)
                .distinct()
                .collect(Collectors.toMap(
                        roomId -> roomId,
                        roomId -> leaseMapper.queryLeaseByRoomId(roomId).size()
                ));
        // 当前生效租约，失效掉本次数量，如果清零了，就需要更新房间状态
        List<Long> roomUpdateList = roomCollect.entrySet().stream()
                .filter(entry -> roomLeaseMap.containsKey(entry.getKey()))
                .filter(entry -> roomLeaseMap.get(entry.getKey()) - entry.getValue() <= 0)
                .map(Map.Entry::getKey)
                .toList();

        if (!CollectionUtils.isEmpty(roomUpdateList)) {
            LambdaUpdateWrapper<RoomPo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(RoomPo::getStatus, 0).in(RoomPo::getId, roomUpdateList);
            roomMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<LeasePo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LeasePo::getIsDelete, 1).in(LeasePo::getId, ids);
        leaseMapper.update(null, updateWrapper);
    }

    @Override
    public LeasePo checkUnique(LeaseVo leaseVo) {
        QueryWrapper<LeasePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", leaseVo.getMemberId()).eq("room_id", leaseVo.getRoomId()).eq("effective", 1).eq("isdelete", 0);
        List<LeasePo> leasePo = leaseMapper.selectList(queryWrapper);
        return leasePo.isEmpty() ? null : leasePo.get(0);
    }

    @Override
    public List<LeaseVo> queryLeaseByRoomId(Long roomId) {
        List<LeaseVo> leaseVos = leaseMapper.queryLeaseByRooomId(roomId);
        return leaseVos;
    }

    private String genLeaseNumber(LeaseVo leaseVo) {
        char firstLetter = leaseVo.getLeaseType().name().charAt(0);
        String roomNo = leaseVo.getRoomNo();
        String initials = CommonUtils.getInitials(leaseVo.getMemberName());
        String yyyyMMdd = DateTimeUtils.formatDate(leaseVo.getStartDate(), "yyyyMMdd");
        return String.join("-", String.valueOf(firstLetter)+roomNo, initials+yyyyMMdd);
    }
}
