package com.panghu.housemanage.service.impl;

import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.Room;
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
    public List<Room> queryRoom(Map<String, Object> params) {
        return roomMapper.queryRoom(params);
    }

}
