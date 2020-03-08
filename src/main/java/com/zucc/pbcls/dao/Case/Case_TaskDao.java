package com.zucc.pbcls.dao.Case;

import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Case_TaskDao extends JpaRepository<Case_Task,Case_Task_pk> {
    Case_Task findAllByCaseTaskpk(Case_Task_pk caseTaskpk);
    List<Case_Task> findByCaseTaskpk_Caseid(int caseid);
}
