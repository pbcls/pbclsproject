package com.zucc.pbcls.service.Project;

import com.zucc.pbcls.dao.LogDao;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.Case.Case_TaskToTask;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.*;

@Service
public class Project_TaskService {
    @Autowired
    Project_TaskDao project_taskDao;
    @Autowired
    Project_TaskToRoleDao project_taskToRoleDao;
    @Autowired
    Project_RoleToUserDao project_roleToUserDao;
    @Autowired
    Project_TaskToTaskDao project_taskToTaskDao;
    @Autowired
    Project_RoleDao project_roleDao;
    @Autowired
    Project_TaskOutputDao project_taskOutputDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    LogDao logDao;

    public Project_TaskOutput findProject_TaskOutput(int projectid,int taskid){
        return project_taskOutputDao.findAllByProjectidAndTaskid(projectid,taskid);
    }

    public List<Project_Task> findalltaskbyprojectid(int projectid){
        return project_taskDao.findByProjectTaskpk_Projectid(projectid);
    }

    public List<Project_Task> findalltaskbyprojectidanduid(int projectid,String uid){
        return project_taskDao.findByProjectidAndUid(projectid,uid);
    }

    public boolean manStartTask(int projectid,int taskid){
        Project_Task task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,taskid));
        if (task.isCanstart()){
            task.setStarttime(new Date());
            task.setStatus(1);
            task.setNeedcheck(false);
            project_taskDao.save(task);
            System.out.println("项目"+task.getProjectTaskpk().getProjectid()+"任务"+task.getProjectTaskpk().getTaskid()+"被开始");
            //给所有参与任务人员发消息
            Project_TaskToRole project_taskToRole = project_taskToRoleDao.findAllByProjectidAndTaskid(projectid,taskid);
            List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectidAndRoleid(projectid,project_taskToRole.getRoleid());
            for (Project_RoleToUser project_roleToUser:project_roleToUsers){
                Log log = new Log();
                log.setType(3);
                log.setProjectid(projectid);
                log.setTaskid(taskid);
                log.setTouid(project_roleToUser.getUid());
                logDao.save(log);
            }
        }else
            return false;
        return true;

    }



    public String showProjectTaskInfo(int projectid,int taskid){
        //找任务信息
        Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,taskid));
        //找前驱任务
        List<Project_TaskToTask> TasksPredecessor = project_taskToTaskDao.findAllByProjectidAndSuccessorid(projectid,taskid);
        Iterator<Project_TaskToTask> iterator_predecessor = TasksPredecessor.iterator();
        while (iterator_predecessor.hasNext()) {
            Project_TaskToTask project_taskToTask = iterator_predecessor.next();
            if (project_taskToTask.getPredecessorid()==0)
                iterator_predecessor.remove();
            else
                project_taskToTask.setTaskname(project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,project_taskToTask.getPredecessorid())).getTaskname());
        }
        //找后继任务
        List<Project_TaskToTask> TasksSuccessor = project_taskToTaskDao.findAllByProjectidAndPredecessorid(projectid,taskid);
        Iterator<Project_TaskToTask> iterator_successor = TasksSuccessor.iterator();
        while (iterator_successor.hasNext()) {
            Project_TaskToTask project_taskToTask = iterator_successor.next();
            if (project_taskToTask.getSuccessorid()==0)
                iterator_successor.remove();
            else
                project_taskToTask.setTaskname(project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,project_taskToTask.getSuccessorid())).getTaskname());
        }
        //找任务对应角色
        Project_TaskToRole project_taskToRole = project_taskToRoleDao.findAllByProjectidAndTaskid(projectid,taskid);
        project_taskToRole.setRolename(project_roleDao.findAllByProjectidAndRoleid(projectid,project_taskToRole.getRoleid()).getRolename());
        //找对应角色的用户
        List<MyUser> users = new ArrayList<>();
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectidAndRoleid(projectid,project_taskToRole.getRoleid());
        for (Project_RoleToUser project_roleToUser:project_roleToUsers){
            MyUser user = userInfoDao.findByUid(project_roleToUser.getUid());
            users.add(user);
        }
        //如果任务完成找对应文件
        List<String> taskOutput = new ArrayList<>();
        if(project_task.getStatus() == 2) {
            Project_TaskOutput project_taskOutput = project_taskOutputDao.findAllByProjectidAndTaskid(projectid, taskid);
            taskOutput = new ProjectFileUtil().getTaskFileList(project_taskOutput);
        }
        //封装成json
        JSONObject json_project_task = new JSONObject(project_task);
        JSONArray json_TasksPredecessor = new JSONArray(TasksPredecessor);
        JSONArray json_TasksSuccessor = new JSONArray(TasksSuccessor);
        JSONObject json_project_taskToRole = new JSONObject(project_taskToRole);
        JSONArray json_users = new JSONArray(users);
        JSONArray json_taskOutput = new JSONArray(taskOutput);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("task",json_project_task);
        jsonObject.put("role",json_project_taskToRole);
        jsonObject.put("predecessor",json_TasksPredecessor);
        jsonObject.put("successor",json_TasksSuccessor);
        jsonObject.put("users",json_users);
        jsonObject.put("output",json_taskOutput);

        return jsonObject.toString();
    }

    public void checkTask(int projectid,int taskid,boolean pass){
        Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,taskid));
        Project_Task lastTask = project_taskDao.findLastTask(projectid);
        Log log = new Log();
        if (pass){
            project_task.setStatus(2);
            project_task.setFinishtime(new Date());
            project_taskDao.save(project_task);
            System.out.println("项目"+project_task.getProjectTaskpk().getProjectid()+"任务"+project_task.getProjectTaskpk().getTaskid()+"被开始");
            if (project_task.getProjectTaskpk()==lastTask.getProjectTaskpk()){
                /**
                 * 这里补充是最后一个任务的评分相关
                 */
            }
            //将上个完成任务提交,并遍历项目查看后续任务是否可以开启
            try {
                new ProjectTaskScheduleService().StartTaskByProjectid(projectid);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            /**
             * 这里还要补全评分相关
             */
            log.setType(9);
            log.setPassstatus(1);
            log.setProjectid(projectid);
            log.setTaskid(taskid);
        }else {
            project_task.setNeedcheck(false);
            project_taskDao.save(project_task);
            log.setType(9);
            log.setPassstatus(2);
            log.setProjectid(projectid);
            log.setTaskid(taskid);
        }
        //给所有参与项目的人发消息
        Project_TaskToRole project_taskToRole = project_taskToRoleDao.findAllByProjectidAndTaskid(projectid,taskid);
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectidAndRoleid(projectid,project_taskToRole.getRoleid());
        for (Project_RoleToUser project_roleToUser:project_roleToUsers){
            log.setTouid(project_roleToUser.getUid());
            logDao.save(log);
        }
        //检查和结束 通过要开启后面的任务 然后上传文档
    }

    public void submitTask(MultipartFile file,int projectid , int taskid){
        //提交任务文件 并且设置needsubmit为false 需要检查为true
        Project_TaskOutput project_taskOutput = project_taskOutputDao.findAllByProjectidAndTaskid(projectid,taskid);
        String filepath = project_taskOutput.getTaskoutput();
        //上传文件,并删掉旧文件
        try {
            new ProjectFileUtil().submitTaskFiles(file.getBytes(),filepath,file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,taskid));
        project_task.setNeedcheck(true);
        project_taskDao.save(project_task);
        //给PM通知
        Log log = new Log();
        log.setType(8);
        log.setProjectid(projectid);
        log.setTaskid(taskid);
        log.setTouid(project_roleToUserDao.findPM(projectid));
        logDao.save(log);
    }




}
