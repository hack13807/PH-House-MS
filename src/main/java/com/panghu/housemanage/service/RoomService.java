package com.panghu.housemanage.service;

import com.panghu.housemanage.pojo.vo.RoomVo;

import java.util.List;
import java.util.Map;

public interface RoomService {
     List<RoomVo> queryRoom(Map<String, Object> params);
     List<RoomVo> queryAllRooms();
}
