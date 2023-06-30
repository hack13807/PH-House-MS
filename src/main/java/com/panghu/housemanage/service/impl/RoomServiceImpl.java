package com.panghu.housemanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.dao.MemberMapper;
import com.panghu.housemanage.dao.RoomMapper;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    MemberMapper memberMapper;

    @Override
    public IPage<RoomVo> pageQueryRoom(Page<RoomVo> page, RoomVo roomVo) {
        return roomMapper.pageQueryRoom(page, roomVo);
    }

    @Override
    public List<RoomPo> getRoomNoSelector(Map<String, Object> params) {
        LambdaQueryWrapper<RoomPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RoomPo::getId, RoomPo::getNumber);
        return roomMapper.selectList(wrapper);
    }

    @Override
    public void batchDelete(Long[] ids) {
        LambdaUpdateWrapper<RoomPo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(RoomPo::getStatus, 0).in(RoomPo::getId, ids);
        roomMapper.update(null, updateWrapper);
    }

    @Override
    public void insertRoom(RoomPo roomPo) {
        roomMapper.insert(roomPo);
    }

    /**
     * 根据更新的租客涉及的房间和现在房间是否存在租客，判断是否将房间状态改为待租
     *
     * @param roomIdlist 租客涉及的房间ids
     */
    @Override
    public void updateRoomStatus(Set<Long> roomIdlist, RoomStatusEnum status) {
        switch (status) {
            case UNUSED -> {
                // 查询改动后，租客所涉及的房间
                List<Long> roomInUseList = memberMapper.selectList(
                        new QueryWrapper<MemberPo>()
                                .select("room_id")
                                .in("room_id", roomIdlist)
                                .notIn("status", 0, 2)
                                .groupBy("room_id")
                ).stream().map(MemberPo::getRoomId).toList();
                List<Long> list = roomIdlist.stream().filter(roomId -> !roomInUseList.contains(roomId)).toList();
                roomMapper.update(null,
                        new UpdateWrapper<RoomPo>()
                                .set("status", RoomStatusEnum.INUSE.getCode())
                                .in("id", roomIdlist)
                );
                if (!list.isEmpty()) {
                    roomMapper.update(null,
                            new UpdateWrapper<RoomPo>()
                                    .set("status", RoomStatusEnum.UNUSED.getCode())
                                    .in("id", list)
                    );
                }
            }
            case INUSE -> {
                roomMapper.update(null,
                        new UpdateWrapper<RoomPo>()
                                .set("status", RoomStatusEnum.INUSE.getCode())
                                .in("id", roomIdlist)
                );
            }
        }
    }

    @Override
    public void updateBatch(List<RoomPo> roomList) {
        roomList.forEach(roomPo -> roomMapper.updateById(roomPo));
    }
}
