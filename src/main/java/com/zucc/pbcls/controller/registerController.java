package com.zucc.pbcls.controller;

import com.zucc.pbcls.dao.UserDao;
import com.zucc.pbcls.pojo.User;
import com.zucc.pbcls.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class registerController {

    @Autowired
    UserService userService;

    @RequestMapping("/toregister")
    public String toregister(){
        return "register";
    }

    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestBody User user){

        System.out.println(user);
        userService.register(user);
        return "login";
    }
}
