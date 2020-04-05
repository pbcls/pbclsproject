package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/student")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;


    @RequestMapping("/toPCenter/{uid}")
    public String PCenter_UserDetail(@PathVariable(value = "uid") String uid, Model model){
        model.addAttribute("uid",uid);
        return "student/PCenter";
    }

    @RequestMapping("/showUserDetial")
    @ResponseBody
    public MyUser showUserDetail(@RequestParam(value = "uid") String uid){
//        UserInfo userInfotest = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String uid = userInfotest.getUsername();
        System.out.println(uid);
        System.out.println(userInfoService.showUserDetail(uid));
        return userInfoService.showUserDetail(uid);
    }


    @RequestMapping("/findallprojectbyuid")
    @ResponseBody
    public String findAllProjectByUid(@RequestParam(value = "uid") String uid){
        return userInfoService.findAllProjectByUid(uid).toString();
    }

    @RequestMapping("/updateUserDetial")
    @ResponseBody
    public boolean updateUserDetial(@RequestBody MyUser user){
        return userInfoService.updateUserDetial(user);
    }

    @RequestMapping("/uploadPortrait")
    @ResponseBody
    public String uploadPortrait(@RequestParam(value = "userImg", required = false) MultipartFile file){
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userInfo.getUsername();
        return userInfoService.uploadPortrait(file,uid);
    }

    /**
     * false 输入密码和原密码不匹配
     * true  修改成功
     */
    @RequestMapping("/changePwd")
    @ResponseBody
    public boolean changePwd(@RequestParam(value = "pwd") String pwd,@RequestParam(value = "newpwd") String newpwd){
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userInfo.getUsername();
        return userInfoService.changePwd(uid,pwd,newpwd);
    }

}
