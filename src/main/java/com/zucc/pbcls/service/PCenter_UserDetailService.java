package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.PCenter_UserDetailDao;
import com.zucc.pbcls.pojo.PCenter_UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PCenter_UserDetailService {
    @Autowired
    PCenter_UserDetailDao pCenter_userDetailDao;

    public PCenter_UserDetail showUserDetail(String uid){
        return pCenter_userDetailDao.findByUid(uid);
    }

    public void updateUserDetial(PCenter_UserDetail pCenter_userDetail){
        pCenter_userDetailDao.save(pCenter_userDetail);

    }
}
