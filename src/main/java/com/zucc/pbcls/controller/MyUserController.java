package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/student")
public class MyUserController {

    @Autowired
    MyUserService myUserService;


    @RequestMapping("/toPCenter")
    public String PCenter_UserDetail(){
        return "student/PCenter";
    }

    @RequestMapping("/showUserDetial")
    @ResponseBody
    public MyUser showUserDetail(){
        UserInfo userInfotest = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userInfotest.getUsername();
        return myUserService.showUserDetail(uid);
    }

    @RequestMapping("/updateUserDetial")
    @ResponseBody
    public int updateUserDetial(@RequestBody MyUser myUser){
        myUserService.updateUserDetial(myUser);
        return 1;
    }

    @RequestMapping("/uploadPortrait")
    @ResponseBody
    public String uploadPortrait(@RequestParam(value = "userImg", required = false) MultipartFile file){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.getUsername();
        return myUserService.uploadPortrait(file,uid);
    }
}
