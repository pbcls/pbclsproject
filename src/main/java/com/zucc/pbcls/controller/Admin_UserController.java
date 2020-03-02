package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.service.Admin_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class Admin_UserController {

    @Autowired
    Admin_UserService admin_userService;

    @RequestMapping("findalluser")
    @ResponseBody
    public List<MyUser> findalluser(){
        return admin_userService.findalluser();
    }
}
