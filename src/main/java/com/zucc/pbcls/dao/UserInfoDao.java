package com.zucc.pbcls.dao;


import com.zucc.pbcls.pojo.MyUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserInfoDao extends JpaRepository<MyUser,String>,JpaSpecificationExecutor<MyUser> {
    MyUser findByUid(String uid);
    List<MyUser> findAll();
}
