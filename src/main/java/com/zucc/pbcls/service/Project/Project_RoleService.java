package com.zucc.pbcls.service.Project;

import com.zucc.pbcls.dao.Project.Project_RoleDao;
import com.zucc.pbcls.pojo.Project.Project_Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Project_RoleService {
    @Autowired
    Project_RoleDao project_roleDao;

    public List<Project_Role> findAllRolesByProjectid(int projectid){
        return project_roleDao.findAllByProjectid(projectid);
    }

    public Project_Role findAllByProjectidAndRoleid(int projectid,int roleid){
        return project_roleDao.findAllByProjectidAndRoleid(projectid,roleid);
    }
}
