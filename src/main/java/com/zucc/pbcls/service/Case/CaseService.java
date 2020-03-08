package com.zucc.pbcls.service.Case;

import com.zucc.pbcls.dao.Case.CaseDao;
import com.zucc.pbcls.dao.Case.Case_RoleDao;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Case.Case_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseService {

    @Autowired
    CaseDao caseDao;


    public List<CaseInfo> findAllCases(){
        return caseDao.findAll();
    }

    public CaseInfo findAllByCaseid(int caseid){
        return caseDao.findAllByCaseid(caseid);
    }






}
