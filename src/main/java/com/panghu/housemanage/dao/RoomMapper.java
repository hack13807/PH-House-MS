package com.panghu.housemanage.dao;

import com.panghu.housemanage.pojo.po.Room;
import com.panghu.housemanage.pojo.vo.RoomVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper {
    List<RoomVo> queryRoom(Map<String, Object> params);

    int insertBatch(List<RoomVo> list);
}
