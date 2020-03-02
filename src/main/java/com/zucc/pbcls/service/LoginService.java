package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.MyUserDao;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {


    @Autowired
    MyUserDao myUserDao;

//    public User login(String uid, String pwd){
//        User user = userDao.findByUid(uid);
//
//        if (new BCryptPasswordEncoder().matches(pwd,user.getPwd()))
//            return user;
//        else
//            return null;
//
//    }

    public boolean register(MyUser myUser){

        if(myUserDao.findByUid(myUser.getUid())!=null)
            return false;
        myUser.setName(myUser.getUid());
        myUser.setPwd(new BCryptPasswordEncoder().encode(myUser.getPwd()));
        myUser.setRole("STUDENT");
        myUser.setAccountNonLocked(true);
        myUser.setPortrait("/img/portrait/default.jpg");

        myUserDao.save(myUser);
        return true;
    }

}
