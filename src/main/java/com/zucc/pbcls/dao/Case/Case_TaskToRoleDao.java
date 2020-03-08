package com.zucc.pbcls.dao.Case;

import com.zucc.pbcls.pojo.Case.Case_TaskToRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Case_TaskToRoleDao extends JpaRepository<Case_TaskToRole,Integer> {
    Case_TaskToRole findAllByCaseidAndTaskid(int caseid,int taskid);
}
