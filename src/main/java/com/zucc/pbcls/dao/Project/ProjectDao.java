package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectDao extends JpaRepository<Project,Integer> {
    List<Project> findAll();
    Project findAllByProjectid(int projectid);
}
