package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.PHResp;
import com.panghu.housemanage.common.util.RequestHandleUtil;
import com.panghu.housemanage.pojo.po.RoomPo;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 房间管理Controller
 */
@Controller
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;

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
        RoomPo roomPo = RequestHandleUtil.buildPoEntity(request, RoomPo.class);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<RoomVo> pageResult = roomService.pageQueryRoom(page, roomPo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtil.successPageResult(pageResult);
    }

}
