package com.zucc.pbcls.service.Project;

import com.zucc.pbcls.dao.Project.Project_RoleToUserDao;
import com.zucc.pbcls.pojo.Project.Project_Role;
import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Project_RoleToUserService {
    @Autowired
    Project_RoleToUserDao project_roleToUserDao;

    public List<Project_RoleToUser> findAllByProjectid(int projectid){
        return project_roleToUserDao.findAllByProjectid(projectid);
    }


}
