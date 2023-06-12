package com.panghu.housemanage.service;

import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.Room;
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
