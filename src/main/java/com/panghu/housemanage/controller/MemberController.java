package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.MemberStatusEnum;
import com.panghu.housemanage.common.util.PageParamTransUtil;
import com.panghu.housemanage.pojo.condition.MemberCondition;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
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

    @GetMapping
    public String search(HttpServletRequest request){
        return "member_list";
    }

    @RequestMapping("/data")
    @ResponseBody
    public Map<String, Object>getData(HttpServletRequest request) {
        Page<MemberVo> page = PageParamTransUtil.getPage(request);
        Map<String, Object> params = new HashMap<>();
        params.put("memberStatus", request.getParameter("memberStatus"));
        params.put("memberName", request.getParameter("memberSearch"));
        params.put("roomNo", request.getParameter("roomSearch"));
        IPage<MemberVo> pageResult = memberService.pageQueryMember(page, params);
        List<MemberVo> rows = pageResult.getRecords().stream().map(member -> member.setMemberStatus(MemberStatusEnum.getValueByCode(Integer.parseInt(member.getMemberStatus())))).toList();
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageResult.getTotal());
        map.put("rows", rows);
        return map;
    }

    @RequestMapping("/deleteData")
    @ResponseBody
    public void deleteData(@RequestParam("ids[]") String[] ids) {
        System.out.println(ids);
    }

}
