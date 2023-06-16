package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import com.panghu.housemanage.common.util.PageParamTransUtil;
import com.panghu.housemanage.pojo.condition.MemberCondition;
import com.panghu.housemanage.pojo.condition.PHBaseCondition;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 房间管理Controller
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    private static final int pageNum = 3;

//    @GetMapping
//    @RequestMapping("/search")
//    public String search(Model model, HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") Integer pn){
////        Page<MemberVo> userPage = new Page<>(pn,2);
////        Page<MemberVo> page = userService.page(userPage,null);
////        page.getRecords()
//        List<MemberVo> members = memberService.queryMember(null);
//        members.stream().map(member -> member.setMemberStatus(MemberStatusEnum.getValueByCode(Integer.valueOf(member.getMemberStatus())))).collect(Collectors.toList());
//        model.addAttribute("list",members);
//        return "member_list";
//    }
    @RequestMapping("/search")
    public String search(Model model, HttpServletRequest request, @RequestBody(required = false) MemberCondition condition){
        Page<MemberVo> page = PageParamTransUtil.getPage(condition);
        IPage<MemberVo> pageResult = memberService.pageQueryMember(page, null);
        pageResult.getRecords().stream().map(member -> member.setMemberStatus(MemberStatusEnum.getValueByCode(Integer.valueOf(member.getMemberStatus())))).collect(Collectors.toList());
        model.addAttribute("page",pageResult);
        model.addAttribute("condition", Optional.ofNullable(condition).orElse(new MemberCondition(1)));
        return "member_list";
    }

    @RequestMapping("/data")
    @ResponseBody
    public Map<String, Object>getData() {
        List<MemberVo> list = new ArrayList<>();
        list.add(MemberVo.builder().memberId(1L).roomId(1L).memberName("房间1").build());
        list.add(MemberVo.builder().memberId(2L).roomId(2L).memberName("房间2").build());
        list.add(MemberVo.builder().memberId(3L).roomId(3L).memberName("房间3").build());
        list.add(MemberVo.builder().memberId(4L).roomId(4L).memberName("房间4").build());
        list.add(MemberVo.builder().memberId(5L).roomId(4L).memberName("房间4").build());
        list.add(MemberVo.builder().memberId(6L).roomId(4L).memberName("房间4").build());
        list.add(MemberVo.builder().memberId(7L).roomId(4L).memberName("房间4").build());
        list.add(MemberVo.builder().memberId(8L).roomId(4L).memberName("房间4").build());
        list.add(MemberVo.builder().memberId(9L).roomId(4L).memberName("房间4").build());
        list.add(MemberVo.builder().memberId(10L).roomId(4L).memberName("房间4").build());
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0000");
        map.put("data", list);
        map.put("total", "12");
        map.put("rows", list);
        return map;
    }

    @RequestMapping("/deleteData")
    @ResponseBody
    public void getData(Long id) {
        System.out.println(id);
    }

}
