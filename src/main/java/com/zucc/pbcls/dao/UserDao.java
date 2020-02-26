package com.zucc.pbcls.dao;


import com.zucc.pbcls.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserDao extends JpaRepository<User,String> {
    User findByUid(String uid);

}
