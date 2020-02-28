package com.zucc.pbcls.controller;


import com.zucc.pbcls.pojo.User;
import com.zucc.pbcls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    UserService userService;


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
    public boolean register(@RequestBody User user){

        System.out.println(user);
        return userService.register(user);
    }

    @RequestMapping("/index")
    public String test( ){
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
