package com.panghu.housemanage.controller;

import com.panghu.housemanage.pojo.po.User;
import com.panghu.housemanage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/housemanage")
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login2")
    public String login(Model model, HttpServletRequest request){
        String name = request.getParameter("username");
        String pwd = request.getParameter("password");

        if ( StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)){
            return "detail_user";
        }

        else {
            User user = User.builder().number(name).pwd(pwd).build();
            User u = userService.queryUser(user);
            if(u==null){
                return ("detail_user");
            }else{
                model.addAttribute("user",u);
                request.getSession().setAttribute("user",u);
                return ("detail_user");
            }
        }
    }

    @GetMapping(value = "/homepage")
    public String homepage(Model model,
                           HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "detail_user";
    }

    @GetMapping(value = "admin/homepage")
    public String adminhomepage(Model model,
                                HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "detail_admin";
    }
}
