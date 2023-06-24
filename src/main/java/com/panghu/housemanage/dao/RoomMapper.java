package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panghu.housemanage.pojo.vo.RoomVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper extends BaseMapper<RoomVo> {
    List<RoomVo> queryRoom(Map<String, Object> params);

    int insertBatch(List<RoomVo> list);
}
