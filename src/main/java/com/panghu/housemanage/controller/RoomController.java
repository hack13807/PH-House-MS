package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtil;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.MemberService;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 房间管理Controller
 */
@Controller
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;
    @Autowired
    MemberService memberService;

    @GetMapping("/page")
    public String getPage(){
        return "room_list";
    }

    @GetMapping
    @ResponseBody
    public PHResp<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
        // 通过前端参数构建分页对象page
        Page<RoomVo> page = RequestHandleUtil.getPage(request);
        // 通过前端参数构建查询实体po
        RoomVo roomVo = RequestHandleUtil.buildPoEntity(request, RoomVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<RoomVo> pageResult = roomService.pageQueryRoom(page, roomVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtil.successPageResult(pageResult);
    }

    @GetMapping("/roomList")
    @ResponseBody
    public PHResp<List<RoomPo>> queryAllRooms(){
        List<RoomPo> rooms = roomService.getRoomNoSelector(null);
        return PHResp.success(rooms);
    }

    @DeleteMapping
    @ResponseBody
    public PHResp<String> deleteData(@RequestBody Long[] ids) {
        roomService.batchDelete(ids);
        return PHResp.success();
    }

    @PutMapping
    @ResponseBody
    public PHResp<String> update(@RequestBody List<RoomVo> volist) {
        List<RoomPo> roomList = RequestHandleUtil.roomDTOTrans(volist);
        // 更新租客信息
        roomService.updateBatch(roomList);
        return PHResp.success();
    }
    @PostMapping
    @ResponseBody
    public PHResp<String> insert(@RequestBody RoomPo roomPo) {
        roomService.insertRoom(roomPo);
        return PHResp.success();
    }

    @GetMapping("/isInUse")
    @ResponseBody
    public PHResp<String> isInUse(Long[] ids){
        List<MemberPo> members = memberService.queryMemberByRoomId(ids);
        if (members.isEmpty()) {
            return PHResp.success();
        }
        String memberNames = String.join(", ", members.stream().map(MemberPo::getName).toList());
        throw new PHServiceException(PHExceptionCodeEnum.ROOM_INUSE, memberNames);
    }
}
