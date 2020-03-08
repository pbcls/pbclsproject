package com.zucc.pbcls.service.Case;

import com.zucc.pbcls.dao.Case.Case_RoleDao;
import com.zucc.pbcls.pojo.Case.Case_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Case_RoleService {
    @Autowired
    Case_RoleDao case_roleDao;

    public List<Case_Role> findAllRoleByCaseid(int caseid){
        return case_roleDao.findAllByCaseid(caseid);
    }

    public Case_Role findRoleByCaseidAndRoleid(int caseid,int roleid){
        return case_roleDao.findAllByCaseidAndRoleid(caseid,roleid);
    }



}
