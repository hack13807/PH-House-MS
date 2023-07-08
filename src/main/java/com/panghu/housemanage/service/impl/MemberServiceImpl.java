package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
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
    public IPage<MemberVo> pageQueryMember(Page<MemberVo> page, MemberVo vo) {
        preprocess(vo);
        return memberMapper.pageQueryMember(page, vo);
    }

    private void preprocess(MemberVo vo) {
        String roomNo = vo.getRoomNo();
        if (roomNo != null && (roomNo.contains(",") || roomNo.contains("，"))) {
            roomNo = roomNo.replace("，", ",");
            vo.setRoomNos(Arrays.asList(roomNo.split(",")));
            vo.setRoomNo(null);
            vo.setVoStatus("-1");
        }
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<MemberPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MemberPo::getIsDelete, 1).in(MemberPo::getId, ids);
        memberMapper.update(null, updateWrapper);
    }

    @Override
    public void insertMember(MemberPo memberPo) {
        memberMapper.insert(memberPo);
    }

    @Override
    public void updateMemberInfo(MemberPo memberPo) {
        memberMapper.updateById(memberPo);
    }


    @Override
    @Transactional
    public void updateBatch(List<MemberPo> memberList) {
        // 更新租客信息
        memberList.forEach(memberPo -> memberMapper.updateById(memberPo));
    }

    @Override
    public List<MemberPo> isTerminate(Long[] ids) {
        List<MemberPo> memberPos = memberMapper.selectBatchIds(Arrays.asList(ids));
        return memberPos.stream().filter(memberPo -> memberPo.getStatus() == 1).toList();
    }

    @Override
    public List<MemberPo> getAllMember() {
        QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "idcard", "name", "tel", "sex").ne("isdelete", 1).ne("status", 2);
        return memberMapper.selectList(queryWrapper);
    }

    @Override
    public List<MemberPo> queryMember(Map<String, Object> params) {
        QueryWrapper<MemberPo> queryWrapper = new QueryWrapper<>();

        // 根据不同属性值拼接查询条件
        if (params.containsKey("id")) {
            Long id = Long.parseLong(params.get("id").toString());
            queryWrapper.eq("id", id);
        }
        if (params.containsKey("name")) {
            String name = (String) params.get("name");
            queryWrapper.eq("name", name);
        }
        if (params.containsKey("sex")) {
            Integer sex = (Integer) params.get("sex");
            queryWrapper.eq("sex", sex);
        }
        if (params.containsKey("tel")) {
            String tel = (String) params.get("tel");
            queryWrapper.eq("tel", tel);
        }
        return memberMapper.selectList(queryWrapper);
    }


    /**
     * 更新房间状态-退租
     *
     * @param memberList 成员列表
     */
    private void updateRoomStatusForTerminate(List<MemberPo> memberList) {
        // 1.当前改动涉及的房间ids
//        Set<Long> roomSet = memberList.stream().map(MemberPo::getRoomId).collect(Collectors.toSet());
//
//        // 2.查询改动的房间ids
//        List<Map<Object, Object>> memberCountsMap = memberMapper.countMemberByRoomId(roomSet.stream().toList());
//        Map<Long, Integer> allCountMap = memberCountsMap.stream()
//                .collect(Collectors.toMap(
//                        entry -> Long.valueOf(entry.get("roomId").toString()), // 转换为Long类型的roomId
//                        entry -> Integer.parseInt(entry.get("count").toString()) // count保持不变，假设已经是Long类型
//                ));
//
//        // 3.遍历退房租客，判断memberCount为0则需要更改房间状态为待租
//        Map<Long, Long> countMap = memberList.stream().collect(Collectors.groupingBy(MemberPo::getRoomId, Collectors.counting()));
//        allCountMap.forEach((roomId, count) -> {
//            if (countMap.containsKey(roomId)) {
//                int updatedCount = count - countMap.get(roomId).intValue();
//                allCountMap.compute(roomId, (key, value) -> updatedCount);
//            }
//        });
//
//        // 4.更新租客数量为0的房间
//        List<Long> updateList = allCountMap.entrySet().stream().filter(entry -> entry.getValue() == 0).map(Map.Entry::getKey).toList();
//        if (!updateList.isEmpty()) {
//            roomService.updateRoomStatus(updateList, RoomStatusEnum.UNUSED);
//        }
    }
}
