package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.service.Admin_CaseService;
import com.zucc.pbcls.service.Case.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Admin_CaseController {
    @Autowired
    Admin_CaseService admin_caseService;

    @RequestMapping("/findcases")
    @ResponseBody
    public Page<CaseInfo> findCases(@RequestParam(value = "statuson")boolean statuson,@RequestParam(value = "statusoff") boolean statusoff,
                                    @RequestParam(value = "findstr") String findstr, @RequestParam(value = "pagenum") int  pagenum,
                                    @RequestParam(value = "pagesize") int pagesize){
        //注意,后台的pagenum是从0开始的,前端显示是从1开始的
        Pageable pageable = PageRequest.of(pagenum-1, pagesize, Sort.Direction.ASC, "caseid");
        Page<CaseInfo> page = admin_caseService.findCases(statuson,statusoff,findstr, pageable);
        return page;

    }

    @RequestMapping("/findprojects")
    @ResponseBody
    public Page<Project> findProjects(@RequestParam(value = "status0")boolean status0, @RequestParam(value = "status1") boolean status1,
                                      @RequestParam(value = "status2") boolean status2,@RequestParam(value = "status3") boolean status3,
                                      @RequestParam(value = "findstr") String findstr, @RequestParam(value = "pagenum") int  pagenum,
                                      @RequestParam(value = "pagesize") int pagesize){
        //注意,后台的pagenum是从0开始的,前端显示是从1开始的
        Pageable pageable = PageRequest.of(pagenum-1, pagesize, Sort.Direction.ASC, "projectid");
        Page<Project> page = admin_caseService.findProjects(status0,status1,status2,status3,findstr, pageable);
        return page;
    }


    @RequestMapping("/updatecasestatus")
    @ResponseBody
    public boolean updateCaseStatus(@RequestParam(value = "caseid") int caseid,@RequestParam(value = "status") boolean status){
        return admin_caseService.updateStatus(caseid, status);
    }

    @RequestMapping("/deletecase")
    @ResponseBody
    public int deleteCase(@RequestParam(value = "caseid") int caseid){
        return admin_caseService.deleteCase(caseid);
    }

    @RequestMapping("/deleteproject")
    @ResponseBody
    public boolean deleteProject(@RequestParam(value = "projectid") int projectid){
        return admin_caseService.deleteProject(projectid);
    }



    /**
     * 案例删除 判断下面是否有项目 如果有只能status置为false 即不能创建新的案例
     * 项目删除 全部删除
     */
}
