package com.zucc.pbcls.controller.Case;

import com.zucc.pbcls.pojo.Case.*;
import com.zucc.pbcls.service.Case.Case_TaskService;
import com.zucc.pbcls.service.Case.Case_TaskToRoleService;
import com.zucc.pbcls.service.Case.Case_TaskToTaskService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class Case_TaskController {
    @Autowired
    Case_TaskService case_taskService;
    @Autowired
    Case_TaskToRoleService case_taskToRoleService;
    @Autowired
    Case_TaskToTaskService case_taskToTaskService;



    @RequestMapping("/showtasklist")
    @ResponseBody
    public List<Case_Task> showTaskList(@RequestParam(value = "caseid") int caseid){
        return case_taskService.findByCaseTaskpk_Caseid(caseid);
    }



    @RequestMapping("/showcasetaskinfo")
    @ResponseBody
    public String showCaseTaskInfo(@RequestBody Case_Task_pk case_task_pk){
        Case_Task case_task = case_taskService.findAllByCaseTaskpk(case_task_pk);
        Case_TaskToRole case_taskToRole= case_taskToRoleService.findRoleByCaseidAndTaskid(case_task_pk);
        List<Case_TaskToTask> TasksSuccessor = case_taskToTaskService.findAllTasksSuccessor(case_taskToRole.getCaseid(),case_taskToRole.getTaskid());
        List<Case_TaskToTask> TasksPredecessor = case_taskToTaskService.findAllTasksPredecessor(case_taskToRole.getCaseid(),case_taskToRole.getTaskid());
        //把获得的数据封装成自定义JSON
        JSONObject json_case_task = new JSONObject(case_task);
        JSONObject json_case_taskToRole = new JSONObject(case_taskToRole);
        JSONArray json_TasksSuccessor = new JSONArray(TasksSuccessor);
        JSONArray json_TasksPredecessor = new JSONArray(TasksPredecessor);
        //封装在一个JSON对象中
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("task",json_case_task);
        jsonObject.put("role",json_case_taskToRole);
        jsonObject.put("successor",json_TasksSuccessor);
        jsonObject.put("predecessor",json_TasksPredecessor);
        return jsonObject.toString();
    }
}
