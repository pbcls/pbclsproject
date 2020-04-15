package com.zucc.pbcls.controller.Project;

import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.Evaluation_Member;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class Project_TaskController {
    @Autowired
    Project_TaskService project_taskService;

    @RequestMapping("/toprojectdettask")
    public String toprojectdettask(){
        return "student/project_detailed_task";
    }

    @RequestMapping("/toprojecttasktest")
    public String toprojecttasktest(){
        return "student/project_task_test";
    }


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

    /**
     * 显示任务信息先判断有没有结束
     * 如果结束了看评价的两个布尔 self是自评 pm是pm评
     * 如果为false即为还没评价 显示评价表单
     * 如果为true即为已经评价 显示评价结果
     */
    @RequestMapping("/showprojecttaskinfo")
    @ResponseBody
    public String showProjectTaskInfo(@RequestBody Project_Task_pk project_task_pk){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = user.getUsername();
        return project_taskService.showProjectTaskInfo(project_task_pk.getProjectid(),project_task_pk.getTaskid(),uid);
    }


    /**
     * 如果是pm自评或者是学生自评,除了评价分数外 projectid,taskid,uid必须被set在传过来的值中  uid为被评价的人的uid
     * 如果是pm评价学生,除了评价分数外 projectid和taskid必须被set在传过来的值中
     */
    @RequestMapping("/evaluatemember")
    @ResponseBody
    public boolean evaluateMember(@RequestBody Evaluation_Member evaluation_member){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String submituid = user.getUsername();
        return project_taskService.evaluateMember(evaluation_member,submituid);
    }



    @RequestMapping("/downloadprojecttaskfile")
    @ResponseBody
    public void downloadProjectTaskFile(@RequestParam(value = "filename") String filename,@RequestParam(value = "projectid") int projectid,
                                 @RequestParam(value = "taskid") int taskid,HttpServletResponse response) {
        Project_TaskOutput project_taskOutput = project_taskService.findProject_TaskOutput(projectid,taskid);
        new ProjectFileUtil().DownloadProjectTaskFile(filename,project_taskOutput,response);
    }

    @RequestMapping("/checktask")
    @ResponseBody
    public void checkTask(@RequestParam(value = "projectid") int projectid,@RequestParam(value = "taskid") int taskid,
                                        @RequestParam(value = "pass") boolean pass) {
        project_taskService.checkTask(projectid,taskid,pass);
    }


    @RequestMapping("/submittask")
    @ResponseBody
    public void submitTask(@RequestParam(value = "submitfile") MultipartFile submitfile, @RequestParam(value = "projectid") int projectid,
                           @RequestParam(value = "taskid") int taskid) {
        project_taskService.submitTask(submitfile,projectid,taskid);
    }


    @RequestMapping("/starttask")
    @ResponseBody
    public boolean startTask(@RequestParam(value = "projectid") int projectid,@RequestParam(value = "taskid") int taskid) {
        return project_taskService.manStartTask(projectid,taskid);
    }








}
