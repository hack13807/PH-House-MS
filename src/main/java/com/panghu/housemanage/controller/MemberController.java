package com.panghu.housemanage.controller;

import com.panghu.housemanage.common.enumeration.RoomStatusEnum;
import com.panghu.housemanage.pojo.po.Member;
import com.panghu.housemanage.service.MemberService;
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
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping
    public String queryMember(Model model, HttpServletRequest request){

        List<Member> members = memberService.queryMember(null);
//        rooms.stream().map(room ->{
//            room.setStatus(RoomStatusEnum.getValueByCode(Integer.valueOf(room.getStatus())));
//            return room;
//        }).collect(Collectors.toList());


        members.stream().map(member -> member.setStatus(RoomStatusEnum.getValueByCode(Integer.valueOf(member.getStatus())))).collect(Collectors.toList());
        model.addAttribute("list",members);
        return "room_list";
    }

}
