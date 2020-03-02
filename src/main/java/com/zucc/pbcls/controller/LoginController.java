package com.zucc.pbcls.controller;


import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping("/")
    public String index(){
        return "redirect:tologin";
    }

    @RequestMapping("/tologin")
    public String tologin(){
        return "login";
    }
    @RequestMapping("/toregister")
    public String toregister(){
        return "register";
    }

    @RequestMapping("/register")
    @ResponseBody
    public boolean register(@RequestBody MyUser myUser){

        System.out.println(myUser);
        return loginService.register(myUser);
    }

    @RequestMapping("/index")
    public String test(){

        UserInfo userInfotest = (UserInfo) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        System.out.println(userInfotest.getMyUserPortrait());


        return "index";
    }

    @RequestMapping("/fail")
    public String fail( ){
        return "fail";
    }



    @RequestMapping("/admin")
    public String admin()  {
        return "admin/admin";
    }
    @RequestMapping("/user")
    public String user()  {
        return "user/user";
    }

    @RequestMapping("/guest")
    public String guest()  {
        return "/guest";
    }




}
