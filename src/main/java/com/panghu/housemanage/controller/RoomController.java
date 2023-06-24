package com.panghu.housemanage.controller;

import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.pojo.vo.RoomVo;
import com.panghu.housemanage.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 房间管理Controller
 */
@Controller
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomService roomService;

    @GetMapping
    public String queryRoom(Model model, HttpServletRequest request){

        List<RoomVo> rooms = roomService.queryRoom(null);
        rooms.stream().map(room -> room.setStatus(RoomStatusEnum.getValueByCode(Integer.valueOf(room.getStatus())))).collect(Collectors.toList());
        model.addAttribute("list",rooms);
        return "room_list";
    }

}
