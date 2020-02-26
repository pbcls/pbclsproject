package com.zucc.pbcls.controller;


import com.zucc.pbcls.pojo.User;
import com.zucc.pbcls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:8080")
@Controller
public class loginController {
    @Autowired
    UserService UserService;


    @RequestMapping("/")
    public String toindex(){
        return "redirect:login";
    }

    @RequestMapping("/login")
    public String authenticationLogin() throws IOException {
        return "login";
    }

    @RequestMapping("/index")
    public String test( ){
        return "index";
    }

    @RequestMapping("/fail")
    public String fail( ){
        return "fail";
    }


//    @RequestMapping("/save")
//    @ResponseBody
//    public String save( ){
//        UserService.save();
//        return "saved";
//
//    }

//    @RequestMapping("/index/{u}-{p}")
//    @ResponseBody
//    public String login(@PathVariable(value = "u") String uid, @PathVariable(value = "p") String pwd ){
//        User User = UserService.login(uid,pwd);
//        if(User == null)
//            return "账号或密码错误";
//        else
//            return "登录成功";
//
//    }

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
