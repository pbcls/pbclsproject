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

    @RequestMapping("/updateUserDetial")
    @ResponseBody
    public int updateUserDetial(@RequestBody MyUser myUser){
        userInfoService.updateUserDetial(myUser);
        return 1;
    }

    @RequestMapping("/uploadPortrait")
    @ResponseBody
    public String uploadPortrait(@RequestParam(value = "userImg", required = false) MultipartFile file){
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userInfo.getUsername();
        return userInfoService.uploadPortrait(file,uid);
    }
}
