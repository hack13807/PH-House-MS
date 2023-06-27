package com.panghu.housemanage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoomMapper extends BaseMapper<RoomPo> {

    int insertBatch(List<RoomVo> list);

    IPage<RoomVo> pageQueryRoom(Page<RoomVo> page, RoomPo po);
}
