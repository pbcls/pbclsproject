package com.zucc.pbcls.service.Project;

import com.zucc.pbcls.dao.Case.*;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.pojo.Case.*;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.utils.ProjectUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    Case_RoleDao case_roleDao;
    @Autowired
    CaseDao caseDao;
    @Autowired
    Case_TaskDao case_taskDao;
    @Autowired
    Case_TaskToTaskDao case_taskToTaskDao;
    @Autowired
    Case_TaskToRoleDao case_taskToRoleDao;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    Project_RoleDao project_roleDao;
    @Autowired
    Project_TaskDao project_taskDao;
    @Autowired
    Project_TaskOutputDao project_taskOutputDao;
    @Autowired
    Project_TaskToTaskDao project_taskToTaskDao;
    @Autowired
    Project_TaskToRoleDao project_taskToRoleDao;


    public void createProjectByCase(int caseid,String projectname){
        CaseInfo caseInfo = caseDao.findAllByCaseid(caseid);
        Project project = new Project();

        //初始化project信息
        project.setCaseid(caseid);
        project.setCasename(caseInfo.getCasename());
        project.setProjectname(projectname);
        project.setDescription(caseInfo.getDescription());
        project.setStatus(1);
        project.setMaxplayer(caseInfo.getMaxplayer());
        //不为null,随便设置的 需要创建后projectid自增才能获取到projectid来命名
        project = projectDao.save(project);
        //真正设置路径的语句
        project.setFoldername("/case"+caseInfo.getCaseid()+"/project"+project.getProjectid());
        projectDao.save(project);

        try {
            new ProjectUploader().InitDirectory(project.getFoldername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置case的已经创建项目的数量
        caseInfo.setInstances(caseInfo.getInstances()+1);
        caseDao.save(caseInfo);

        //把case_role信息给project
        List<Case_Role> case_roles = case_roleDao.findAllByCaseid(caseid);
        for (Case_Role case_role:case_roles){
            Project_Role project_role = new Project_Role();
            project_role.setProjectid(project.getProjectid());
            project_role.setRoleid(case_role.getRoleid());
            project_role.setRolename(case_role.getRolename());
            project_roleDao.save(project_role);
        }

        //把case_task信息给project
        List<Case_Task> case_tasks = case_taskDao.findByCaseTaskpk_Caseid(caseid);
        for (Case_Task case_task:case_tasks){
            Project_Task project_task = new Project_Task();

            Project_Task_pk project_task_pk = new Project_Task_pk();
            project_task_pk.setProjectid(project.getProjectid());
            project_task_pk.setTaskid(case_task.getCaseTaskpk().getTaskid());

            project_task.setProjectTaskpk(project_task_pk);
            project_task.setTaskname(case_task.getTaskname());
            project_task.setDescription(case_task.getDescription());
            project_task.setDuration(case_task.getDutarion());
            project_taskDao.save(project_task);
        }
        //把case_tasktorole信息给project
        List<Case_TaskToRole> case_taskToRoles = case_taskToRoleDao.findAllByCaseid(caseid);
        for (Case_TaskToRole case_taskToRole:case_taskToRoles){
            Project_TaskToRole project_taskToRole = new Project_TaskToRole();
            project_taskToRole.setProjectid(project.getProjectid());
            project_taskToRole.setTaskid(case_taskToRole.getTaskid());
            project_taskToRole.setProject_role(new Project_Role(project.getProjectid(),case_taskToRole.getCase_role().getRoleid(),case_taskToRole.getCase_role().getRolename()));
            project_taskToRoleDao.save(project_taskToRole);
        }

        //把case_case_tasktotask信息给project
        List<Case_TaskToTask> case_taskToTasks = case_taskToTaskDao.findAllByCaseid(caseid);
        for (Case_TaskToTask case_taskToTask:case_taskToTasks){
            Project_TaskToTask project_taskToTask = new Project_TaskToTask();
            project_taskToTask.setProjectid(project.getProjectid());
            project_taskToTask.setPredecessorid(case_taskToTask.getPredecessorid());
            project_taskToTask.setSuccessorid(case_taskToTask.getSuccessorid());
            project_taskToTask.setType(case_taskToTask.getType());
            project_taskToTask.setTaskname(case_taskToTask.getTaskname());
            project_taskToTaskDao.save(project_taskToTask);
        }

        List<Project_TaskToTask> project_taskToTasks = project_taskToTaskDao.findAllByProjectid(project.getProjectid());
        List<Project_Task> project_tasks = project_taskDao.findByProjectTaskpk_Projectid(project.getProjectid());


        /**
         * 在这里根据任务关系列表和任务列表计算并对任务表中的是否为关键任务赋值
         */



        //设置project_taskoutput,并创建文件夹
        for (Project_Task project_task : project_tasks){
            Project_TaskOutput project_taskOutput = new Project_TaskOutput();
            project_taskOutput.setProjectid(project_task.getProjectTaskpk().getProjectid());
            project_taskOutput.setTaskid(project_task.getProjectTaskpk().getTaskid());
            project_taskOutput.setTaskoutput(project.getFoldername()+"/task"+project_task.getProjectTaskpk().getTaskid());
            project_taskOutputDao.save(project_taskOutput);

            try {
                new ProjectUploader().InitDirectory(project_taskOutput.getTaskoutput());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void startProject(int projectid){
        Project project = projectDao.findAllByProjectid(projectid);


    }



    public List<Project> findAllProjects(){
        return projectDao.findAll();
    }

    public Project findByProjectid(int projectid){
        return projectDao.findAllByProjectid(projectid);
    }


}
