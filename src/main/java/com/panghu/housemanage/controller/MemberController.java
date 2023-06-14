package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/aaa")
    public String queryMember2(Model model, HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") Integer pn){
//        Page<MemberVo> userPage = new Page<>(pn,2);
//        Page<MemberVo> page = userService.page(userPage,null);
//        page.getRecords()
        List<MemberVo> members = memberService.queryMember(null);
        members.stream().map(member -> member.setMemberStatus(MemberStatusEnum.getValueByCode(Integer.valueOf(member.getMemberStatus())))).collect(Collectors.toList());
        model.addAttribute("list",members);
        return "member_list";
    }
    @GetMapping
    public String queryMember(Model model, HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") Integer pn){
        Page<MemberVo> page = new Page<>(1,2);
        List<MemberVo> members2 = memberService.queryMember(null);
        IPage<MemberVo> members = memberService.pageQueryMember(page, null);
//        members.stream().map(member -> member.setMemberStatus(MemberStatusEnum.getValueByCode(Integer.valueOf(member.getMemberStatus())))).collect(Collectors.toList());
        model.addAttribute("list",members);
        return "member_list";
    }

}
