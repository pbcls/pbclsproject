package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.User;
import com.zucc.pbcls.pojo.PCenter_UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PCenter_UserDetailDao extends JpaRepository<PCenter_UserDetail,String> {
    PCenter_UserDetail findByUid(String uid);
}
