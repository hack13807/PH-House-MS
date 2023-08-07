package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.PHExceptionCodeEnum;
import com.panghu.housemanage.common.exception.PHServiceException;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.MemberService;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
        Page<RoomVo> page = RequestHandleUtils.getPage(request);
        // 通过前端参数构建查询实体po
        RoomVo roomVo = RequestHandleUtils.buildPoEntity(request, RoomVo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<RoomVo> pageResult = roomService.pageQueryRoom(page, roomVo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtils.successPageResult(pageResult);
    }

    @GetMapping("/roomList")
    @ResponseBody
    public PHResp<List<RoomPo>> queryAllRooms(@RequestParam(required = false, value = "status") String status){
        List<RoomPo> rooms = roomService.getRoomNoSelector(status == null ? null :  Map.of("status", status));
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
        // 验重
        volist.forEach(roomVo -> {
            RoomPo po = roomService.checkUnique(roomVo);
            if (po != null) {
                throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_ROOM, null);
            }
        });
        List<RoomPo> roomList = RequestHandleUtils.roomDTOTrans(volist);
        // 更新信息
        roomService.updateBatch(roomList);
        return PHResp.success();
    }
    @PostMapping
    @ResponseBody
    public PHResp<String> insert(@RequestBody RoomVo roomVo) {
        // 验重
        RoomPo po = roomService.checkUnique(roomVo);
        if (po != null) {
            throw new PHServiceException(PHExceptionCodeEnum.UNIQUE_ROOM, null);
        }
        List<RoomPo> roomList = RequestHandleUtils.roomDTOTrans(Collections.singletonList(roomVo));
        roomService.insertRoom(roomList.get(0));
        return PHResp.success();
    }
}
