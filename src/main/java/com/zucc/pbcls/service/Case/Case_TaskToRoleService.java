package com.zucc.pbcls.service.Case;

import com.zucc.pbcls.dao.Case.Case_TaskToRoleDao;
import com.zucc.pbcls.pojo.Case.Case_TaskToRole;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Case_TaskToRoleService {
    @Autowired
    Case_TaskToRoleDao case_taskToRoleDao;


    public Case_TaskToRole findRoleByCaseidAndTaskid(Case_Task_pk case_task_pk){
        return case_taskToRoleDao.findAllByCaseidAndTaskid(case_task_pk.getCaseid(),case_task_pk.getTaskid());
    }



}
