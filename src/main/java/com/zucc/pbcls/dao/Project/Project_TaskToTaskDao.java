package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_TaskToTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Project_TaskToTaskDao extends JpaRepository<Project_TaskToTask,Integer> {
    List<Project_TaskToTask> findAllByProjectid(int projectid);
}
