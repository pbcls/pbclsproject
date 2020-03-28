package com.zucc.pbcls.controller.Project;

import com.zucc.pbcls.dao.Case.CaseDao;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.pojo.Project.Project_Role;
import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.Project.ProjectService;
import com.zucc.pbcls.service.Project.Project_RoleService;
import com.zucc.pbcls.service.Project.Project_RoleToUserService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class ProjectController {
    @Autowired
    ProjectService projectService;
    @Autowired
    CaseDao caseDao;
    @Autowired
    Project_RoleService project_roleService;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    Project_RoleToUserService project_roleToUserService;

    @RequestMapping("/tochatroom")
    public String tochatroom(){
        return "chat.html";
    }

    @RequestMapping("/toproject")
    public String toproject(){
        return "student/project";
    }

    @RequestMapping("/findallprojects")
    @ResponseBody
    //这里返回caseinfo的原因是前端项目分类显示,每个案例下面都有一个对应的项目的List
    public List<CaseInfo> findAllProjects(){
        return projectService.findAllProjects();
    }

    //需要增加自定义JSON内容
    @RequestMapping("/showprojectinfo")
    @ResponseBody
    public String showProjectInfo(@RequestParam(value = "projectid") int projectid){
        Project project = projectService.findByProjectid(projectid);
        CaseInfo caseInfo = caseDao.findAllByCaseid(project.getCaseid());
        List<Project_Role> project_roles = project_roleService.findAllRolesByProjectid(projectid);
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserService.findAllByProjectid(projectid);
        for (Project_RoleToUser project_roleToUser:project_roleToUsers){
            project_roleToUser.setRolename(project_roleService.findAllByProjectidAndRoleid(projectid,project_roleToUser.getRoleid()).getRolename());
            project_roleToUser.setUser(userInfoDao.findByUid(project_roleToUser.getUid()));
        }
        //把获得的数据封装成自定义JSON
        JSONObject json_caseinfo = new JSONObject(caseInfo);
        JSONObject json_project = new JSONObject(project);
        JSONArray json_project_roles = new JSONArray(project_roles);
        JSONArray json_project_roleToUsers = new JSONArray(project_roleToUsers);
        //封装在一个JSON对象中
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("case",json_caseinfo);
        jsonObject.put("project",json_project);
        jsonObject.put("roles",json_project_roles);
        jsonObject.put("role_to_user",json_project_roleToUsers);

        return jsonObject.toString();
    }


    @RequestMapping("/createprojectbycase")
    @ResponseBody
    public void createProjectByCase(@RequestParam(value = "caseid") int caseid,@RequestParam(value = "projectname") String projectname){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        projectService.createProjectByCase(user.getUsername(),caseid, projectname);
    }

    @RequestMapping("/findunfinishedprojectsbyuid")
    @ResponseBody
    public String findUnfinishedProjectsByUid(){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.findUnfinishedProjectsByUid(user.getUsername()).toString();
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
     * 0 成功申请 申请进列表
     * 1 项目已经开始 无法申请
     * 2 项目成员已满 无法申请
     */
    @RequestMapping("/applyproject")
    @ResponseBody
    public int applyProject(@RequestParam(value = "projectid") int projectid, @RequestParam(value = "roleid") int roleid){
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
            projectFileUtil.uploadDOCSFiles(docfile.getBytes(),project.getFoldername()+"/DOCS/",docfile.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/checkPM")
    @ResponseBody
    public boolean checkPM(@RequestParam(value = "projectid") int projectid){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = user.getUsername();
        return projectService.checkPM(projectid,uid);
    }





}
