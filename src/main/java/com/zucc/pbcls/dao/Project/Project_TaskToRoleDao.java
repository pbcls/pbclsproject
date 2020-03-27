package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_TaskToRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Project_TaskToRoleDao extends JpaRepository<Project_TaskToRole,Integer> {
    List<Project_TaskToRole> findAllByProjectid(int projectid);
    Project_TaskToRole findAllByProjectidAndTaskid(int projectid,int taskid);
}
