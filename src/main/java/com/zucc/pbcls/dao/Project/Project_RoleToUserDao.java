package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_Role;
import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Project_RoleToUserDao extends JpaRepository<Project_RoleToUser,Integer> {
    List<Project_RoleToUser> findAllByUid(String uid);
    List<Project_RoleToUser> findAllByProjectid(int projectid);
    Project_RoleToUser findAllByUidAndProjectid(String uid,int projectid);
    List<Project_RoleToUser> findAllByProjectidAndRoleid(int projectid,int roleis);

    @Query(nativeQuery = true,value ="SELECT COUNT(uid) FROM project_roletouser WHERE projectid=(:projectid)")
    int countByProjectid(@Param("projectid")int projectid);

    @Query(nativeQuery = true,value = "SELECT uid from project_roletouser,project_role where project_roletouser.roleid=project_role.roleid" +
            "            AND project_role.projectid=project_roletouser.projectid AND rolename LIKE '项目经理'AND project_role.projectid=(:projectid);")
    String findPM(@Param("projectid") int projectid);

    @Query(nativeQuery = true,value = "SELECT * from project_roletouser,project_role where project_roletouser.roleid=project_role.roleid " +
            "AND project_role.projectid=project_roletouser.projectid AND rolename LIKE '教师'AND project_role.projectid=(:projectid);")
    Project_RoleToUser findTeacher(@Param("projectid") int projectid);



}
