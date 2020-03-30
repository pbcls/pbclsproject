package com.zucc.pbcls.config;


import com.zucc.pbcls.Listener.ExcelListener;
import com.zucc.pbcls.dao.Evaluation_MemberDao;
import com.zucc.pbcls.dao.LogDao;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
//该类作用为,将Listener,Filter等不被spring托管的类中的对象注入,以实现该些类中的Autowired注解生效
public class ListenerInitRun implements CommandLineRunner {

    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    Project_TaskDao project_taskDao;
    @Autowired
    Project_TaskToRoleDao project_taskToRoleDao;
    @Autowired
    Project_TaskToTaskDao project_taskToTaskDao;
    @Autowired
    Project_RoleDao project_roleDao;
    @Autowired
    Project_RoleToUserDao project_roleToUserDao;
    @Autowired
    LogDao logDao;
    @Autowired
    Evaluation_MemberDao evaluation_memberDao;

    @Override
    public void run(String... args) throws Exception {
        //解决listener注入不了spring容器对象的问题
        ExcelListener.setUserInfoDao(userInfoDao);
        ProjectTaskScheduleService.setProjectDao(projectDao);
        ProjectTaskScheduleService.setProject_taskDao(project_taskDao);
        ProjectTaskScheduleService.setProject_taskToRoleDao(project_taskToRoleDao);
        ProjectTaskScheduleService.setProject_taskToTaskDao(project_taskToTaskDao);
        ProjectTaskScheduleService.setProject_roleDao(project_roleDao);
        ProjectTaskScheduleService.setProject_roleToUserDao(project_roleToUserDao);
        ProjectTaskScheduleService.setLogDao(logDao);
        ProjectTaskScheduleService.setEvaluation_memberDao(evaluation_memberDao);
    }
}

