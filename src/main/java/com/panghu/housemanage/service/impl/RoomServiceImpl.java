package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
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

    @Override
    public IPage<RoomVo> pageQueryRoom(Page<RoomVo> page, RoomPo roomPo) {
        return roomMapper.pageQueryRoom(page, roomPo);
    }

    @Override
    public List<RoomPo> getRoomNoSelector(Map<String, Object> params) {
        LambdaQueryWrapper<RoomPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RoomPo::getId, RoomPo::getNumber);
        return roomMapper.selectList(wrapper);
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<RoomPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(RoomPo::getStatus, 0).in(RoomPo::getId, ids);
        roomMapper.update(null, updateWrapper);
    }

    @Override
    public void insertRoom(RoomPo roomPo) {
        roomMapper.insert(roomPo);
    }

    @Override
    public void updateRoomInfo(RoomPo roomPo) {
        roomMapper.updateById(roomPo);
    }

}
