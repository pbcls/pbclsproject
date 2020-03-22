package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_TaskToTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Project_TaskToTaskDao extends JpaRepository<Project_TaskToTask,Integer> {
    List<Project_TaskToTask> findAllByProjectid(int projectid);
    @Query(nativeQuery = true,value ="SELECT * FROM project_tasktotask WHERE  projectid=(:projectid) AND successorid<>0 AND predecessorid<>0")
    List<Project_TaskToTask> findAllMidByProjectid(@Param("projectid")int projectid);

    List<Project_TaskToTask> findAllByProjectidAndPredecessorid(int projectid, int predecessorid);
    List<Project_TaskToTask> findAllByProjectidAndSuccessorid(int projectid, int successorid);
    @Query(nativeQuery = true,value ="SELECT COUNT(pttid) FROM project_tasktotask WHERE  projectid=(:projectid) AND successorid<>0 AND predecessorid<>0")
    int countarcnum(@Param("projectid")int projectid);
}
