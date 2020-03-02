package com.zucc.pbcls.dao;


import com.zucc.pbcls.pojo.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MyUserDao extends JpaRepository<MyUser,String> {
    MyUser findByUid(String uid);
}
