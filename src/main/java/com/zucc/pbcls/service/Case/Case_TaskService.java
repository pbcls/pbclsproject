package com.zucc.pbcls.service.Case;

import com.zucc.pbcls.dao.Case.Case_TaskDao;
import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Case_TaskService {
    @Autowired
    Case_TaskDao case_taskDao;

    public List<Case_Task> findByCaseTaskpk_Caseid(int caseid){
        return  case_taskDao.findByCaseTaskpk_Caseid(caseid);
    }

    public Case_Task findAllByCaseTaskpk(Case_Task_pk case_task_pk){
        return case_taskDao.findAllByCaseTaskpk(case_task_pk);
    }
}
