package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.MyUserDao;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Admin_UserService {

    @Autowired
    MyUserDao myUserDao;

    public List<MyUser> findalluser(){
        return myUserDao.findAll();
    }

//    public List<User> findByAccountNonLocked(){
////        return userDao.findByAccountNonLocked(false);
//    }




}
