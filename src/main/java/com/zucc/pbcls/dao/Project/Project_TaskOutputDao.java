package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_TaskOutput;
import com.zucc.pbcls.pojo.Project.Project_Task_pk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Project_TaskOutputDao extends JpaRepository<Project_TaskOutput,Integer> {
}
