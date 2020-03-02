package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.PCenter_UserDetailDao;
import com.zucc.pbcls.dao.UserDao;
import com.zucc.pbcls.pojo.PCenter_UserDetail;
import com.zucc.pbcls.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;

@Service
public class UserService {


    @Autowired
    UserDao userDao;

    @Autowired
    PCenter_UserDetail pCenter_userDetail;

    @Autowired
    PCenter_UserDetailDao pCenter_userDetailDao;

    public User login(String uid, String pwd){
        User user = userDao.findByUid(uid);

        if (new BCryptPasswordEncoder().matches(pwd,user.getPwd()))
            return user;
        else
            return null;

    }

    public boolean register(User user){

        if(userDao.findByUid(user.getUid())!=null)
            return false;
        user.setName(user.getUid());
        user.setRole("STUDENT");
        user.setPwd(new BCryptPasswordEncoder().encode(user.getPwd()));
        user.setAccountNonLocked(true);
        userDao.save(user);

        pCenter_userDetail.setUid(user.getUid());
        pCenter_userDetailDao.save(pCenter_userDetail);
        return true;
    }

}
