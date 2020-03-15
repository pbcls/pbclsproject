package com.zucc.pbcls.controller.Project;

import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.service.Project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @RequestMapping("/findallprojects")
    @ResponseBody
    public List<Project> findAllProjects(){
        return projectService.findAllProjects();
    }

    //需要增加自定义JSON内容
    @RequestMapping("/findbyprojectid")
    @ResponseBody
    public Project findAllProjects(@RequestParam(value = "projectid") int projectid){
        return projectService.findByProjectid(projectid);
    }


    @RequestMapping("/findbyprojectid")
    @ResponseBody
    public void createProjectByCase(@RequestParam(value = "caseid") int caseid,@RequestParam(value = "projectname") String projectname){
        projectService.createProjectByCase(caseid, projectname);
    }
}
