package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_Task;
import com.zucc.pbcls.pojo.Project.Project_Task_pk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Project_TaskDao extends JpaRepository<Project_Task,Project_Task_pk> {
    List<Project_Task> findByProjectTaskpk_Projectid(int projectid);
}
