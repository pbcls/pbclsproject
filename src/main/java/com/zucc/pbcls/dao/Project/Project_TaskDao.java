package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_Task;
import com.zucc.pbcls.pojo.Project.Project_Task_pk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Project_TaskDao extends JpaRepository<Project_Task,Project_Task_pk> {
    List<Project_Task> findByProjectTaskpk_Projectid(int projectid);
    Project_Task findByProjectTaskpk(Project_Task_pk project_task_pk);
    @Query(nativeQuery = true,value ="SELECT * FROM project_task WHERE taskid in (SELECT successorid FROM project_tasktotask WHERE project_tasktotask.predecessorid = 0 AND projectid=(:projectid)) AND projectid=(:projectid)")
    List<Project_Task> findAllFirstTasks(@Param("projectid")int projectid);
    @Query(nativeQuery = true,value ="SELECT * FROM project_task WHERE taskid in (SELECT predecessorid FROM project_tasktotask WHERE project_tasktotask.successorid = 0 AND projectid=(:projectid)) AND projectid=(:projectid)")
    Project_Task findLastTask(@Param("projectid")int projectid);

    List<Project_Task> findByProjectTaskpk_ProjectidAndStatus(int projectid,int status);
    List<Project_Task> findByProjectTaskpk_ProjectidAndStatusAndCanstart(int projectid,int status,boolean canstart);

    @Query(nativeQuery = true,value = "select * FROM project_task WHERE projectid=(:projectid) AND taskid in (" +
            "    select taskid FROM project_tasktorole WHERE project_tasktorole.projectid = (:projectid) AND  roleid in(" +
            "        select roleid FROM project_roletouser WHERE project_roletouser.projectid = (:projectid) AND uid like (:uid)" +
            "        )" +
            "    )")
    List<Project_Task> findByProjectidAndUid(@Param("projectid")int projectid,@Param("uid")String uid);
}
