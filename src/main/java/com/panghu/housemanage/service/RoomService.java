package com.panghu.housemanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.RoomVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoomService {
    IPage<RoomVo> pageQueryRoom(Page<RoomVo> page, RoomVo roomVo);

    List<RoomPo> getRoomNoSelector(Map<String, Object> params);

    void batchDelete(Long[] ids);

    void insertRoom(RoomPo roomPo);

    void updateRoomStatus(List<Long> roomIdList, RoomStatusEnum status);

    void updateBatch(List<RoomPo> roomList);
}
