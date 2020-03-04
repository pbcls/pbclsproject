package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.Admin_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<MyUser> findUsers(@RequestParam(value = "needid") boolean needuid, @RequestParam(value = "needname") boolean needname,
                                  @RequestParam(value = "needmail") boolean needmail, @RequestParam(value = "findstr") String findstr,
                                  @RequestParam(value = "notAccountNonLocked") boolean notAccountNonLocked, @RequestParam(value = "isAccountNonLocked") boolean isAccountNonLocked,
                                  @RequestParam(value = "role") String role, @RequestParam(value = "pagenum") int  pagenum,
                                  @RequestParam(value = "pagesize") int pagesize){

        Pageable pageable = PageRequest.of(pagenum, pagesize, Sort.Direction.ASC, "registerTime");
        Page<MyUser> page = admin_userService.findUsers(needuid,needname,needmail,findstr,
                notAccountNonLocked,isAccountNonLocked,role,pageable);
        return page;
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

    @RequestMapping("/resetPwd")
    @ResponseBody
    public boolean resetPwd(@RequestParam("uid") String uid){
        return admin_userService.resetPwd(uid);
    }

}
