package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.Room;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper {
    List<Room> queryRoom(Map<String, Object> params);

    int insertBatch(List<Room> list);
}
