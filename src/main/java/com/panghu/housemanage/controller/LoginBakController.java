package com.panghu.housemanage.controller;

import com.panghu.housemanage.pojo.po.UserBak;
import com.panghu.housemanage.service.UserBakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/managebooks")
public class LoginBakController {
    @Autowired
    UserBakService userService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/detail")
    public String detail(Model model,
                         HttpServletRequest request) {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        UserBak user;
        if (userName == null) return "login";
        if (userName.contains("admin_")) {
            user = new UserBak(userName, password);
            UserBak u = userService.checkManager(user);
            if (u == null) return "login";
            model.addAttribute("user", u);
            request.getSession().setAttribute("user", u);
            return "detail_admin";
        } else {
            user = new UserBak(userName, password);
            UserBak u = userService.checkUser(user);
            if (u == null) return "login";
            model.addAttribute("user", u);
            request.getSession().setAttribute("user", u);
            return "detail_user";
        }
    }

//    @GetMapping(value = "/homepage")
//    public String homepage(Model model,
//                           HttpServletRequest request){
//        UserBak user = (UserBak) request.getSession().getAttribute("user");
//        model.addAttribute("user", user);
//        return "detail_user";
//    }
//
//    @GetMapping(value = "admin/homepage")
//    public String adminhomepage(Model model,
//                           HttpServletRequest request){
//        UserBak user = (UserBak) request.getSession().getAttribute("user");
//        model.addAttribute("user", user);
//        return "detail_admin";
//    }

}
