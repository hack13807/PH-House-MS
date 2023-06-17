package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.util.RequestHandleUtil;
import com.panghu.housemanage.pojo.po.MemberPo;
import com.panghu.housemanage.pojo.vo.MemberVo;
import com.panghu.housemanage.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 房间管理Controller
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping
    public String search(HttpServletRequest request){
        return "member_list";
    }

    @RequestMapping("/data")
    @ResponseBody
    public Map<String, Object> getData(@RequestBody MemberPo memberPo) {
        // 通过前段传过来的po实体构建分页对象page
        Page<MemberVo> page = RequestHandleUtil.getPage(memberPo);
        // 把分页对象page和查询实体po传到service层，查询结果返回封装成Page对象
        IPage<MemberVo> pageResult = memberService.pageQueryMember(page, memberPo);
        // 获取查询总数和记录，构建返回前端的Map对象
        return RequestHandleUtil.buildResultMap(pageResult);
    }

    @RequestMapping("/deleteData")
    @ResponseBody
    public void deleteData(@RequestParam("ids[]") String[] ids) {
        System.out.println(ids);
    }

}
