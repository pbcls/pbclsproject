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
    List<MyUser> findAllByAccountNonLocked(boolean AccountNonLocked);
    List<MyUser> findAllByEmail(String email);
    List<MyUser> findAllByName(String Name);
    List<MyUser> findAllByRole(String role);

    List<MyUser> findAllByNameLikeOrEmailLikeOrRoleLike(String findstr1,String findstr2,String findstr3);

}
