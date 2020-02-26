package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.UserDao;
import com.zucc.pbcls.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    UserDao userDao;

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
        user.setRole("USER");
        user.setPwd(new BCryptPasswordEncoder().encode(user.getPwd()));
        userDao.save(user);
        return true;
    }

}
