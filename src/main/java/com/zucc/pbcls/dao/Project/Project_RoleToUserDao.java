package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Project_RoleToUserDao extends JpaRepository<Project_RoleToUser,Integer> {
    List<Project_RoleToUser> findAllByUid(String uid);
    List<Project_RoleToUser> findAllByProjectid(int projectid);
    Project_RoleToUser findAllByUidAndProjectid(String uid,int projectid);

    @Query(nativeQuery = true,value ="SELECT COUNT(uid) FROM project_roletouser WHERE projectid=(:projectid)")
    int countByProjectid(@Param("projectid")int projectid);

    @Query(nativeQuery = true,value = "SELECT prtu.uid from project_roletouser prtu,project_role pr where prtu.projectid = (:projectid) and pr.projectid=prtu.projectid and pr.rolename LIKE '项目经理'")
    String findPM(@Param("projectid") int projectid);
}
