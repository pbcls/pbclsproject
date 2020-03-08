package com.zucc.pbcls.dao.Case;

import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_TaskToTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Case_TaskToTaskDao extends JpaRepository<Case_TaskToTask,Integer> {
    List<Case_TaskToTask> findAllByCaseidAndPredecessorid(int caseid,int predecessorid);
    List<Case_TaskToTask> findAllByCaseidAndSuccessorid(int caseid,int successorid);
}
