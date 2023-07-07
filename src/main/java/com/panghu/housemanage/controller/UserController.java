package com.panghu.housemanage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panghu.housemanage.common.enumeration.UserStatusEnum;
import com.panghu.housemanage.common.util.RequestHandleUtils;
import com.panghu.housemanage.pojo.vo.UserVo;
import com.panghu.housemanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public String queryUser() {
        return "user_list";
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Map<String, Object> queryUser(HttpServletRequest request) {
        Page<UserVo> page = RequestHandleUtils.getPage(null);
        Map<String, Object> param = new HashMap<>();
        param.put("userStatus", request.getParameter("userStatus"));
        param.put("userName", request.getParameter("userName"));
        IPage<UserVo> pageResult = userService.listUser(page, param);
        List<UserVo> rows = pageResult.getRecords().stream().map(user -> user.setUserStatus(UserStatusEnum.getValueByCode(Integer.parseInt(user.getUserStatus())))).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("total", pageResult.getTotal());
        map.put("rows", rows);
        return map;
    }
}