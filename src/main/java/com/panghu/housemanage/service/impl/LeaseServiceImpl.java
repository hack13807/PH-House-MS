package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.EffectiveEnum;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
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
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.LeaseService;
import com.panghu.housemanage.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
            vo.setVoEffective("-1");
        }
    }

    @Override
    @Transactional
    public void insertLease(LeaseVo leaseVo) {
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
        // 新增租约记录
        leaseMapper.insert(leasePo);
        // 检查是否需要更新租客和房间状态
        updateMemberStatusForInsert(leasePo);
        updateRoomStatusForInsert(leasePo);
    }



    private void updateMemberStatusForInsert(LeasePo leasePo) {
        Long memberId = leasePo.getMemberId();
        List<LeaseVo> voList = leaseMapper.queryLeaseByMemberId(memberId);
        List<LeaseVo> list = voList.stream().filter(leaseVo -> leaseVo.getEffective() == EffectiveEnum.EFFECTIVE).toList();
        if (CollectionUtils.isEmpty(list)) {
            // TODO 不存在活跃租约，需要更改状态
        }
    }
    private void updateRoomStatusForInsert(LeasePo leasePo) {

    }

    @Override
    @Transactional
    public void updateBatch(List<LeasePo> poList) {
        if (poList.get(0).getEffective() != null && poList.get(0).getEffective() == 0) {
            // 只有失效场景才会使effective为0发起put请求
            updateMemberStatus(poList);
            updateRoomStatus(poList);
        }
        poList.forEach(leasePo -> leaseMapper.updateById(leasePo));
    }

    /**
     * 更新租客状态
     *
     * @param poList 订单列表
     */
    private void updateMemberStatus(List<LeasePo> poList) {
        // 租客分组
        Map<Long, Long> collect = poList.stream().collect(Collectors.groupingBy(LeasePo::getMemberId, Collectors.counting()));
        // 获取当前租客生效的租约
        Map<Long, Integer> memberLeaseMap = poList.stream()
                .map(LeasePo::getMemberId)
                .distinct()
                .collect(Collectors.toMap(
                        memberId -> memberId,
                        memberId -> leaseMapper.queryLeaseByMemberId(memberId).size()
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
    }
    private void updateRoomStatus(List<LeasePo> poList) {
        // 房间分组
        Map<Long, Long> collect = poList.stream().collect(Collectors.groupingBy(LeasePo::getRoomId, Collectors.counting()));
        // 获取当前房间生效的租约
        Map<Long, Integer> roomLeaseMap = poList.stream()
                .map(LeasePo::getRoomId)
                .distinct()
                .collect(Collectors.toMap(
                        roomId -> roomId,
                        roomId -> leaseMapper.queryLeaseByRoomId(roomId).size()
                ));
        // 当前生效租约，失效掉本次数量，如果清零了，就需要更新房间状态
        List<Long> updateList = collect.entrySet().stream()
                .filter(entry -> roomLeaseMap.containsKey(entry.getKey()))
                .filter(entry -> roomLeaseMap.get(entry.getKey()) - entry.getValue() <= 0)
                .map(Map.Entry::getKey)
                .toList();

        if (!CollectionUtils.isEmpty(updateList)) {
            LambdaUpdateWrapper<RoomPo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(RoomPo::getStatus, 0).in(RoomPo::getId, updateList);
            roomMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<LeasePo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(LeasePo::getIsDelete, 1).in(LeasePo::getId, ids);
        leaseMapper.update(null, updateWrapper);
    }

    private String genLeaseNumber(LeaseVo leaseVo) {
        char firstLetter = leaseVo.getLeaseType().name().charAt(0);
        String roomNo = leaseVo.getRoomNo();
        String initials = CommonUtils.getInitials(leaseVo.getMemberName());
        String yyyyMMdd = DateTimeUtils.formatDate(leaseVo.getStartDate(), "yyyyMMdd");
        return String.join("-", String.valueOf(firstLetter)+roomNo, initials+yyyyMMdd);
    }
}
