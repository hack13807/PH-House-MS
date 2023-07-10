package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
