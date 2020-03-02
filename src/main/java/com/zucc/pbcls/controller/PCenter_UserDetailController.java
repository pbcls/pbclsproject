package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.PCenter_UserDetail;
import com.zucc.pbcls.service.PCenter_UserDetailService;
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
public class PCenter_UserDetailController {

    @Autowired
    PCenter_UserDetailService pCenter_userDetailService;

    @Autowired
    PCenter_UserDetail pCenter_userDetail;

    @RequestMapping("/toPCenter")
    public String PCenter_UserDetail(){
        return "student/PCenter";
    }

    @RequestMapping("/showUserDetial")
    @ResponseBody
    public PCenter_UserDetail showUserDetail(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.getUsername();
        pCenter_userDetail = pCenter_userDetailService.showUserDetail(uid);
        return pCenter_userDetail;
    }

    @RequestMapping("/updateUserDetial")
    @ResponseBody
    public int updateUserDetial(@RequestBody PCenter_UserDetail pcud){
        pCenter_userDetailService.updateUserDetial(pcud);
        return 1;
    }

    @RequestMapping("/uploadPortrait")
    @ResponseBody
    public String uploadPortrait(@RequestParam(value = "userImg", required = false) MultipartFile file){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = userDetails.getUsername();
        return pCenter_userDetailService.uploadPortrait(file,uid);
    }
}
