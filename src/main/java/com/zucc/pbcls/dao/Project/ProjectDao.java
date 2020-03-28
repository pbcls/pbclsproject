package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProjectDao extends JpaRepository<Project,Integer> ,JpaSpecificationExecutor<Project> {
    List<Project> findAll();
    List<Project> findAllByCaseid(int caseid);
    List<Project> findAllByStatus(int status);
    Project findAllByProjectid(int projectid);
}
