package com.zucc.pbcls.controller.Project;

import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.Project.ProjectService;
import com.zucc.pbcls.utils.CaseDOCSDownloader;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @RequestMapping("/tochatroom")
    public String tochatroom(){
        return "chat.html";
    }

    @RequestMapping("/toproject")
    public String todoc(){
        return "student/project";
    }

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


    @RequestMapping("/createprojectbycase")
    @ResponseBody
    public void createProjectByCase(@RequestParam(value = "caseid") int caseid,@RequestParam(value = "projectname") String projectname){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        projectService.createProjectByCase(user.getUsername(),caseid, projectname);
    }

    @RequestMapping("/findprojectsbyuid")
    @ResponseBody
    public String findProjectsByUid(){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.findAllByUid(user.getUsername()).toString();
    }

    @RequestMapping("/isenteredproject")
    @ResponseBody
    public boolean isEnteredProject(@RequestParam(value = "projectid") int projectid){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.isEnteredProject(user.getUsername(),projectid);
    }

    @RequestMapping("/showprojectlog")
    @ResponseBody
    public List<Log> showProjectLog(){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.showProjectLog(user.getUsername());
    }

    /**
     * false maxplayer满了
     */
    @RequestMapping("/judgeapply")
    @ResponseBody
    public boolean judgeApply(@RequestParam(value = "logid") int logid,
                           @RequestParam(value = "projectid") int projectid,
                           @RequestParam(value = "roleid") int roleid,
                           @RequestParam(value = "memberid") String memberid,
                           @RequestParam(value = "pass")boolean pass){
        return projectService.judgeApply(logid,projectid, roleid, memberid, pass);
    }

    /**
     * false maxplayer满了
     */
    @RequestMapping("/applyproject")
    @ResponseBody
    public boolean applyProject(@RequestParam(value = "projectid") int projectid, @RequestParam(value = "roleid") int roleid){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.applyProject(user.getUsername(),projectid,roleid);
    }


    /**
     * false 至少有一个角色没有被使用
     */
    @RequestMapping("/startproject")
    @ResponseBody
    public boolean startProject(@RequestParam(value = "projectid") int projectid){
        return projectService.startProject(projectid);
    }

    //以下是关于任务文档的显示
    @RequestMapping("/getprojectdocslist")
    @ResponseBody
    public List<String> getCaseFileList(@RequestParam(value = "projectid") int projectid) {
        Project project = projectService.findByProjectid(projectid);
        ProjectFileUtil projectFileUtil = new ProjectFileUtil();
        System.out.println(projectFileUtil.getProjectFileList(project));
        return projectFileUtil.getProjectFileList(project);
    }


    @RequestMapping("/downloadprojectdocsfile")
    @ResponseBody
    public void DownloadCaseFile(@RequestParam(value = "docfile") String docfile,@RequestParam(value = "projectid") int projectid,HttpServletResponse response) {
        Project project = projectService.findByProjectid(projectid);
        ProjectFileUtil projectFileUtil = new ProjectFileUtil();
        projectFileUtil.DownloadProjectFile(docfile,project,response);
    }



    @RequestMapping("/uploadprojectdocfile")
    @ResponseBody
    public void uploadProjectDocFile(@RequestParam(value = "projectid") int projectid,@RequestParam(value = "docfile") MultipartFile docfile) {
        Project project = projectService.findByProjectid(projectid);
        ProjectFileUtil projectFileUtil = new ProjectFileUtil();
        try {
            projectFileUtil.uploadFiles(docfile.getBytes(),project.getFoldername()+"/DOCS/",docfile.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
