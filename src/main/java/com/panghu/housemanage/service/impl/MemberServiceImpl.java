package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.MemberOptTypeEnum;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.PHBaseVo;
import com.panghu.housemanage.service.MemberService;
import com.panghu.housemanage.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberMapper memberMapper;
    @Autowired
    RoomService roomService;

    @Override
    public IPage<MemberVo> pageQueryMember(Page<MemberVo> page, PHBaseVo vo) {
        return memberMapper.pageQueryMember(page, vo);
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<MemberPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MemberPo::getStatus, 0).in(MemberPo::getId, ids);
        memberMapper.update(null, updateWrapper);
    }

    @Override
    public void insertMember(MemberPo memberPo) {
        memberMapper.insert(memberPo);
        roomService.updateRoomStatus(Collections.singletonList(memberPo.getRoomId()), RoomStatusEnum.INUSE);
    }

    @Override
    public void updateMemberInfo(MemberPo memberPo) {
        memberMapper.updateById(memberPo);
    }

    @Override
    public List<MemberPo> queryMemberByRoomId(Long[] ids) {
        QueryWrapper<MemberPo> wrapper = new QueryWrapper<>();
        wrapper.in("room_id", ids);
        return memberMapper.selectList(wrapper);


    }

    @Override
    @Transactional
    public void updateBatch(List<MemberPo> memberList, Integer optType) {

        switch (MemberOptTypeEnum.getTypeEnumBytypeId(optType)) {
            case MODIFY -> {
                // 编辑
                updateRoomStatusForModify(memberList);
            }
            case TERMINATE -> {
                // 退租
                updateRoomStatusForTerminate(memberList);
            }
        }
        // 更新租客信息
        memberList.forEach(memberPo -> memberMapper.updateById(memberPo));
    }

    @Override
    public List<MemberPo> isTerminate(Long[] ids) {
        List<MemberPo> memberPos = memberMapper.selectBatchIds(Arrays.asList(ids));
        return memberPos.stream().filter(memberPo -> memberPo.getStatus() == 1).toList();
    }

    /**
     * 更新房间状态-编辑
     *
     * @param memberList 成员列表
     */
    private void updateRoomStatusForModify(List<MemberPo> memberList) {
        Long newRoomId = memberList.get(0).getRoomId();
        // 租客原房间id
        Long oldRoomId = memberMapper.selectById(memberList.get(0).getId()).getRoomId();
        // 房间有变化需处理房间状态
        if (newRoomId.compareTo(oldRoomId) != 0) {
            roomService.updateRoomStatus(Collections.singletonList(newRoomId), RoomStatusEnum.INUSE);
            List<Map<Object, Object>> memberCountsMap = memberMapper.countMemberByRoomId(Set.of(oldRoomId).stream().toList());
            Map<Object, Object> countMap = memberCountsMap.get(0);
            int count = Integer.parseInt(countMap.get("count").toString());
            if (count == 1) {
                // 原来只有一名租客，编辑房间后应该改为待租状态
                roomService.updateRoomStatus(Collections.singletonList(oldRoomId), RoomStatusEnum.UNUSED);
            }
        }
    }

    /**
     * 更新房间状态-退租
     *
     * @param memberList 成员列表
     */
    private void updateRoomStatusForTerminate(List<MemberPo> memberList) {
        // 1.当前改动涉及的房间ids
        Set<Long> roomSet = memberList.stream().map(MemberPo::getRoomId).collect(Collectors.toSet());

        // 2.查询改动的房间ids
        List<Map<Object, Object>> memberCountsMap = memberMapper.countMemberByRoomId(roomSet.stream().toList());
        Map<Long, Integer> allCountMap = memberCountsMap.stream()
                .collect(Collectors.toMap(
                        entry -> Long.valueOf(entry.get("roomId").toString()), // 转换为Long类型的roomId
                        entry -> Integer.parseInt(entry.get("count").toString()) // count保持不变，假设已经是Long类型
                ));

        // 3.遍历退房租客，判断memberCount为0则需要更改房间状态为待租
        Map<Long, Long> countMap = memberList.stream().collect(Collectors.groupingBy(MemberPo::getRoomId, Collectors.counting()));
        allCountMap.forEach((roomId, count) -> {
            if (countMap.containsKey(roomId)) {
                int updatedCount = count - countMap.get(roomId).intValue();
                allCountMap.compute(roomId, (key, value) -> updatedCount);
            }
        });

        // 4.更新租客数量为0的房间
        List<Long> updateList = allCountMap.entrySet().stream().filter(entry -> entry.getValue() == 0).map(Map.Entry::getKey).toList();
        if (!updateList.isEmpty()) {
            roomService.updateRoomStatus(updateList, RoomStatusEnum.UNUSED);
        }
    }
}
