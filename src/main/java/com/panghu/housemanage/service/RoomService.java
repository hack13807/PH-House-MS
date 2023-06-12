package com.panghu.housemanage.service;

import com.panghu.housemanage.pojo.Room;

import java.util.List;
import java.util.Map;

public interface RoomService {
     List<Room> queryRoom(Map<String, Object> params);
}
