package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.LeasePo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.LeaseVo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    MemberMapper memberMapper;

    @Override
    public IPage<RoomVo> pageQueryRoom(Page<RoomVo> page, RoomVo roomVo) {
        return roomMapper.pageQueryRoom(page, roomVo);
    }

    @Override
    public List<RoomPo> getRoomNoSelector(Map<String, Object> params) {
        LambdaQueryWrapper<RoomPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RoomPo::getId, RoomPo::getNumber);
        wrapper.eq(RoomPo::getEnable, 1);
        if (!CollectionUtils.isEmpty(params) && !ObjectUtils.isEmpty(params.get("status"))) {
            wrapper.eq(RoomPo::getStatus, params.get("status"));
        }
        return roomMapper.selectList(wrapper);
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<RoomPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(RoomPo::getEnable, 0).in(RoomPo::getId, ids);
        roomMapper.update(null, updateWrapper);
    }

    @Override
    public void insertRoom(RoomPo roomPo) {
        roomMapper.insert(roomPo);
    }

    @Override
    public void updateRoomStatus(List<Long> roomIdList, RoomStatusEnum status) {
                roomMapper.update(null,
                        new UpdateWrapper<RoomPo>()
                                .set("status", status.getCode())
                                .in("id", roomIdList)
                );

    }

    @Override
    public void updateBatch(List<RoomPo> roomList) {
        roomList.forEach(roomPo -> roomMapper.updateById(roomPo));
    }

    @Override
    public RoomPo checkUnique(RoomVo roomVo) {
        RoomPo currentRoom = roomMapper.selectById(roomVo.getRowId()); // 根据房间ID获取原始数据
        // 判断房间号是否与原始数据中的房间号一致
        if (!currentRoom.getNumber().equals(roomVo.getRoomNo())) {
            // 验证提交上来的新房间号是否已存在
            QueryWrapper<RoomPo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("number", roomVo.getRoomNo());
            List<RoomPo> roomPos = roomMapper.selectList(queryWrapper);
            return roomPos.isEmpty() ? null : roomPos.get(0);
        }
        return null;
    }
}
