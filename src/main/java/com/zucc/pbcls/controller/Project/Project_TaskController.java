package com.zucc.pbcls.controller.Project;

import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.Project.Project_Task;
import com.zucc.pbcls.pojo.Project.Project_TaskOutput;
import com.zucc.pbcls.pojo.Project.Project_Task_pk;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.Project.Project_TaskService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class Project_TaskController {
    @Autowired
    Project_TaskService project_taskService;


    /**
     * 先读status 判断任务是未开始还是进行中还是结束
     * 如果是未开始 则判断canstart字段 来设置是否显示开始按钮
     */
    @RequestMapping("/findalltaskbyprojectid")
    @ResponseBody
    public List<Project_Task> findAllTaskByProjectid(@RequestParam(value = "projectid") int projectid){
        return project_taskService.findalltaskbyprojectid(projectid);
    }

    @RequestMapping("/findalltaskbyprojectidanduid")
    @ResponseBody
    //以用户为单位,即在项目中找我需要参与的任务
    public List<Project_Task> findalltaskbyprojectidanduid(@RequestParam(value = "projectid") int projectid){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = user.getUsername();
        return project_taskService.findalltaskbyprojectidanduid(projectid,uid);
    }

    @RequestMapping("/showprojecttaskinfo")
    @ResponseBody
    public String showProjectTaskInfo(@RequestBody Project_Task_pk project_task_pk){
        return project_taskService.showProjectTaskInfo(project_task_pk.getProjectid(),project_task_pk.getTaskid());
    }



    @RequestMapping("/downloadprojecttaskfile")
    @ResponseBody
    public void downloadProjectTaskFile(@RequestParam(value = "filename") String filename,@RequestParam(value = "projectid") int projectid,
                                 @RequestParam(value = "taskid") int taskid,HttpServletResponse response) {
        Project_TaskOutput project_taskOutput = project_taskService.findProject_TaskOutput(projectid,taskid);
        new ProjectFileUtil().DownloadProjectTaskFile(filename,project_taskOutput,response);
    }









}
