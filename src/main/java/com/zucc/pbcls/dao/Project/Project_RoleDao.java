package com.zucc.pbcls.dao.Project;

import com.zucc.pbcls.pojo.Project.Project_Role;
import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Project_RoleDao extends JpaRepository<Project_Role,Integer>{
    Project_Role findAllByProjectidAndRoleid(int projectid,int roleid);
    Project_Role findAllByProjectidAndRolename(int projectid,String rolename);

    @Query(nativeQuery = true,value = "select * from project_role where not exists(" +
            "    select roleid from project_role where not exists("+
            "            select roleid from project_roletouser where project_role.roleid=project_roletouser.roleid and project_roletouser.projectid = (:projectid)" +
            "        )" +
            "    )")
    List<Project_Role> isAllRoleUsed(@Param("projectid") int projectid);

    List<Project_Role> findAllByProjectid(int projectid);
}
