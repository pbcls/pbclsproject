package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.Admin_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class Admin_UserController {

    @Autowired
    Admin_UserService admin_userService;

    @RequestMapping("/toadmin_user")
    public String toadmin_user(){
        return "admin/admin_user";
    }

    @RequestMapping("/findUsers")
    @ResponseBody
    public List<MyUser> findUsers(@RequestParam("findstr") String findstr){
//        return admin_userService.findUsers(findstr);
        return null;
    }


    @RequestMapping("/findAllUsers")
    @ResponseBody
    public List<MyUser> findAllUsers(){
        return admin_userService.findAllUsers();
    }


    @RequestMapping("/findAllLockedUsers")
    @ResponseBody
    public List<MyUser> findAllLockedUsers(){
        return admin_userService.findAllLockedUsers();
    }

    @RequestMapping("/deleteUser")
    @ResponseBody
    public boolean deleteUser(@RequestParam("uid") String uid){
        return admin_userService.deleteUser(uid);
    }

    @RequestMapping("/lockUser")
    @ResponseBody
    public boolean lockUser(@RequestParam("uid") String uid){
        return admin_userService.lockUser(uid);
    }

    @RequestMapping("/unlockUser")
    @ResponseBody
    public boolean unlockUser(@RequestParam("uid") String uid){
        return admin_userService.unlockUser(uid);
    }

    @RequestMapping("/changeRole")
    @ResponseBody
    public boolean changeRole(@RequestParam("uid") String uid,@RequestParam("role")String Role){
        return admin_userService.changeRole(uid,Role);
    }







}
