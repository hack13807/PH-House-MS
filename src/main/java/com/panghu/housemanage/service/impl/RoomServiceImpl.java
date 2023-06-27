package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public IPage<RoomVo> pageQueryRoom(Page<RoomVo> page, RoomPo po) {
        return roomMapper.pageQueryRoom(page, po);
    }

}
