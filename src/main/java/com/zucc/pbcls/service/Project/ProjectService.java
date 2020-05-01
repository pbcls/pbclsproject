package com.zucc.pbcls.service.Project;

import com.zucc.pbcls.dao.*;
import com.zucc.pbcls.dao.Case.*;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.pojo.AOE.ALGraph;
import com.zucc.pbcls.pojo.AOE.ArcNode;
import com.zucc.pbcls.pojo.AOE.VNode;
import com.zucc.pbcls.pojo.Case.*;
import com.zucc.pbcls.pojo.Evaluation_Member;
import com.zucc.pbcls.pojo.Evaluation_Mutual;
import com.zucc.pbcls.pojo.Evaluation_Team;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    CaseDao caseDao;
    @Autowired
    Case_RoleDao case_roleDao;
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
    @Autowired
    Project_RoleToUserDao project_roleToUserDao;
    @Autowired
    LogDao logDao;
    @Autowired
    Evaluation_MemberDao evaluation_memberDao;
    @Autowired
    Evaluation_MutualDao evaluation_mutualDao;
    @Autowired
    Evaluation_TeamDao evaluation_teamDao;


    public void createProjectByCase(String uid, int caseid, String projectname) {
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
        project.setFoldername("/case" + caseInfo.getCaseid() + "/project" + project.getProjectid());
        projectDao.save(project);

        //初始化创建project下面的task文件夹和DOCS文件夹,并将Case的DOCS下的文件拷贝到project下的DOCS中
        try {
            ProjectFileUtil projectFileUtil = new ProjectFileUtil();
            projectFileUtil.InitDirectory(project.getFoldername());
            String olddocfilepath = caseInfo.getFoldername() + "/DOCS";
            String newdocfilepath = project.getFoldername() + "/DOCS";
            projectFileUtil.InitDirectory(newdocfilepath);
            new ProjectFileUtil().copyCDocToPDoc(olddocfilepath,newdocfilepath);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //设置case的已经创建项目的数量
        caseInfo.setInstances(caseInfo.getInstances() + 1);
        caseDao.save(caseInfo);

        //把case_role信息给project
        List<Case_Role> case_roles = case_roleDao.findAllByCaseid(caseid);
        for (Case_Role case_role : case_roles) {
            Project_Role project_role = new Project_Role();
            project_role.setProjectid(project.getProjectid());
            project_role.setRoleid(case_role.getRoleid());
            project_role.setRolename(case_role.getRolename());
            project_roleDao.save(project_role);
        }

        //把case_task信息给project
        List<Case_Task> case_tasks = case_taskDao.findByCaseTaskpk_Caseid(caseid);
        for (Case_Task case_task : case_tasks) {
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
        for (Case_TaskToRole case_taskToRole : case_taskToRoles) {
            Project_TaskToRole project_taskToRole = new Project_TaskToRole();
            project_taskToRole.setProjectid(project.getProjectid());
            project_taskToRole.setTaskid(case_taskToRole.getTaskid());
            project_taskToRole.setRoleid(case_taskToRole.getCase_role().getRoleid());
            project_taskToRoleDao.save(project_taskToRole);
        }

        //把case_case_tasktotask信息给project
        List<Case_TaskToTask> case_taskToTasks = case_taskToTaskDao.findAllByCaseid(caseid);
        for (Case_TaskToTask case_taskToTask : case_taskToTasks) {
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
        ALGraph G = new ALGraph();
        System.out.println("以下是查找图的关键路径的程序。");
        G = CreateALGraph(G, project.getProjectid());
        CriticalPath(G);
        //最后一个结束点需要单独赋值
        Project_TaskToTask lasttaskToTask = project_taskToTaskDao.findAllByProjectidAndSuccessorid(project.getProjectid(),0).get(0);
        Project_Task lasttask = project_taskDao.findByProjectTaskpk(new Project_Task_pk(project.getProjectid(),lasttaskToTask.getPredecessorid()));
        //找出最后一个节点得所有前驱任务节点
        List<Project_TaskToTask> projectTaskToTasks = project_taskToTaskDao.findAllByProjectidAndSuccessorid(project.getProjectid(),lasttask.getProjectTaskpk().getTaskid());
        //找出前面得最早开始时间加上任务时长最大的任务点,即为除最终节点以外的最后一个关键路径的点
        int max = 0;
        for (Project_TaskToTask project_taskToTask:project_taskToTasks){
            if (project_taskToTask.getPredecessorid()!=0) {
                Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(project.getProjectid(), project_taskToTask.getPredecessorid()));
                if (max < (project_task.getEarlystart() + project_task.getDuration()))
                    max = project_task.getEarlystart() + project_task.getDuration();
            }
        }
        lasttask.setIscritical(true);
        lasttask.setEarlystart(max);
        lasttask.setLatestart(max);
        project_taskDao.save(lasttask);


        //设置project_taskoutput,并创建文件夹
        for (Project_Task project_task : project_tasks) {
            Project_TaskOutput project_taskOutput = new Project_TaskOutput();
            project_taskOutput.setProjectid(project_task.getProjectTaskpk().getProjectid());
            project_taskOutput.setTaskid(project_task.getProjectTaskpk().getTaskid());
            project_taskOutput.setTaskoutput(project.getFoldername() + "/task" + project_task.getProjectTaskpk().getTaskid());
            project_taskOutputDao.save(project_taskOutput);

            try {
                new ProjectFileUtil().InitDirectory(project_taskOutput.getTaskoutput());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //将创建者设置为该项目PM
        Project_RoleToUser project_roleToUser = new Project_RoleToUser();
        project_roleToUser.setProjectid(project.getProjectid());
        System.out.println("项目经理"+project.getProjectid()+","+project_roleDao.findAllByProjectidAndRolename(project.getProjectid(), "项目经理"));
        project_roleToUser.setRoleid(project_roleDao.findAllByProjectidAndRolename(project.getProjectid(), "项目经理").getRoleid());
        project_roleToUser.setUid(uid);
        project_roleToUserDao.save(project_roleToUser);

    }


    public List<CaseInfo> findAllProjects() {
        List<CaseInfo> caseInfos = caseDao.findAll();
        for (CaseInfo caseInfo : caseInfos)
            caseInfo.setProjects(projectDao.findAllByCaseid(caseInfo.getCaseid()));
        return caseInfos;
    }

    public Project findByProjectid(int projectid) {
        return projectDao.findAllByProjectid(projectid);
    }

    public JSONArray findUnfinishedProjectsByUid(String uid) {
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByUid(uid);
        Iterator<Project_RoleToUser> iterator = project_roleToUsers.iterator();
        while (iterator.hasNext()) {
            Project_RoleToUser project_roleToUser = iterator.next();
            int projectstatus = projectDao.findAllByProjectid(project_roleToUser.getProjectid()).getStatus();
            //把已经完成的删掉 只留下未开始和正在进行的
            if (projectstatus == 0|| projectstatus == 3)
                iterator.remove();
        }
        JSONArray json_projects = new JSONArray();
        for (int i = 0; i < project_roleToUsers.size(); i++) {
            project_roleToUsers.get(i).setRolename(project_roleDao.findAllByProjectidAndRoleid(project_roleToUsers.get(i).getProjectid()
                    , project_roleToUsers.get(i).getRoleid()).getRolename());
            JSONObject json_project_roleToUser = new JSONObject(project_roleToUsers.get(i));
            JSONObject json_project = new JSONObject(projectDao.findAllByProjectid(project_roleToUsers.get(i).getProjectid()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("project", json_project);
            jsonObject.put("role", json_project_roleToUser);
            json_projects.put(i, jsonObject);
        }
        return json_projects;
    }

    public boolean isEnteredProject(String uid, int projectid) {
        Project_RoleToUser project_roleToUser = project_roleToUserDao.findAllByUidAndProjectid(uid, projectid);
        if (project_roleToUser != null)
            return true;
        else
            return false;
    }

    /**
     * false maxplayer满了
     */
    public List<Log> showProjectLog(String uid, Pageable pageable) {
        List<Log> logs = logDao.findAllByTouid(uid,pageable);
        for (Log log : logs) {
            if (log.getCaseid() != 0){
                CaseInfo caseInfo = caseDao.findAllByCaseid(log.getCaseid());
                if (caseInfo!=null)
                    log.setCasename(caseInfo.getCasename());
            }
            if (log.getProjectid() != 0) {
                Project project = projectDao.findAllByProjectid(log.getProjectid());
                if (project!=null)
                    log.setProjectname(project.getProjectname());
            }
            if (log.getTaskid() != 0) {
                Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(log.getProjectid(), log.getTaskid()));
                if (project_task!=null)
                    log.setTaskname(project_task.getTaskname());
            }
            if (log.getRoleid() != 0) {
                Project_Role project_role=project_roleDao.findAllByProjectidAndRoleid(log.getProjectid(), log.getRoleid());
                if (project_role!=null)
                    log.setRolename(project_role.getRolename());
            }
            if (!("").equals(log.getUid()) && log.getUid() != null)
                log.setUsername(userInfoDao.findByUid(log.getUid()).getName());
            if (!("").equals(log.getTouid()) && log.getTouid() != null)
                log.setTousername(userInfoDao.findByUid(log.getTouid()).getName());
        }
        return logs;
    }

    /**
     * 0 成功申请 申请进列表
     * 1 项目已经开始 无法申请
     * 2 项目成员已满 无法申请
     */
    public int applyProject(String uid, int projectid, int roleid) {
        //获取项目信息
        Project project = projectDao.findAllByProjectid(projectid);
        if (2==project.getStatus())
            return 1;
        //获取项目中存在的人数
        int player = project_roleToUserDao.countByProjectid(projectid);
        //获取申请的角色
        Project_Role project_role = project_roleDao.findAllByProjectidAndRoleid(projectid,roleid);

        if ("教师".equals(project_role.getRolename())) {
            Project_RoleToUser teacher = project_roleToUserDao.findTeacher(projectid);
            if (teacher!=null)
                return 2;//超过最大人数:这个项目已经有老师了
        }else {
            if (player >= projectDao.findAllByProjectid(projectid).getMaxplayer())
                return 2;//超过最大人数
        }
        //为教师已经有教师了和不为教师(为学生)超过最大人数情况已经判断完了 剩下的就是可以直接申请
        Log log = new Log();
        log.setProjectid(projectid);
        log.setRoleid(roleid);
        log.setUid(uid);
        log.setTouid(project_roleToUserDao.findPM(projectid));
        log.setType(1);
        log.setNeedpass(true);
        log.setPassstatus(0);
        logDao.save(log);
        return 0;
    }

    /**
     * false maxplayer满了
     */
    public boolean judgeApply(int logid, int projectid, int roleid, String memberid, boolean pass) {
        int player = project_roleToUserDao.countByProjectid(projectid);
        Log oldlog = logDao.findByLogid(logid);
        Log log = new Log();
        log.setType(2);
        log.setNeedpass(false);
        log.setTouid(memberid);
        log.setProjectid(projectid);
        log.setRoleid(roleid);
        if (pass) {
            if (player >= projectDao.findAllByProjectid(projectid).getMaxplayer())
                return false;
            Project_RoleToUser project_roleToUser = new Project_RoleToUser();
            project_roleToUser.setProjectid(projectid);
            project_roleToUser.setRoleid(roleid);
            project_roleToUser.setUid(memberid);
            project_roleToUserDao.save(project_roleToUser);
            log.setPassstatus(1);
            oldlog.setPassstatus(1);
        } else {
            log.setPassstatus(2);
            oldlog.setPassstatus(2);
        }
        logDao.save(oldlog);
        logDao.save(log);
        return true;
    }

    /**
     * false 至少有一个角色没有被使用
     */
    public boolean startProject(int projectid) {
        List<Project_Role> project_roles = project_roleDao.isAllRoleUsed(projectid);
        if (project_roles.isEmpty())
            return false;
        Project project = projectDao.findAllByProjectid(projectid);
        CaseInfo caseInfo = caseDao.findAllByCaseid(project.getCaseid());
        caseInfo.setStartedinstances(caseInfo.getStartedinstances() + 1);
        caseDao.save(caseInfo);
        project.setStatus(2);
        project.setStarttime(new Date());
        projectDao.save(project);


        /**
         * 在这里开启第一批任务
         */
        try {
            new ProjectTaskScheduleService().StartTaskByProjectid(projectid);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //初始化任务自评表
        List<Project_Task> projectTasks = project_taskDao.findByProjectTaskpk_Projectid(project.getProjectid());
        for (Project_Task project_task:projectTasks){
            Project_TaskToRole project_taskToRole = project_taskToRoleDao.findAllByProjectidAndTaskid(
                    project_task.getProjectTaskpk().getProjectid(),project_task.getProjectTaskpk().getTaskid());
            List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectidAndRoleid(
                    project_taskToRole.getProjectid(),project_taskToRole.getRoleid());
            for (Project_RoleToUser projectRoleToUser:project_roleToUsers){
                Evaluation_Member emember = new Evaluation_Member();
                emember.setProjectid(project_task.getProjectTaskpk().getProjectid());
                emember.setTaskid(project_task.getProjectTaskpk().getTaskid());
                emember.setRoleid(projectRoleToUser.getRoleid());
                emember.setUid(projectRoleToUser.getUid());
                emember.setSelfEvaluated(false);
                emember.setPmEvaluated(false);
                evaluation_memberDao.save(emember);
            }
        }

        //初始化互评表
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectid(projectid);
        //删掉teacher
        Iterator<Project_RoleToUser> iterator = project_roleToUsers.iterator();
        while (iterator.hasNext()) {
            Project_RoleToUser project_roleToUser = iterator.next();
            Project_RoleToUser teacher = project_roleToUserDao.findTeacher(projectid);
            if (project_roleToUser.getUid().equals(teacher.getUid()))
                iterator.remove();
        }


        for (Project_RoleToUser self:project_roleToUsers){
            //删掉自己
            List<Project_RoleToUser> userExceptSelfs = new ArrayList<Project_RoleToUser>(project_roleToUsers);

            Iterator<Project_RoleToUser> iterator_userExceptSelf = userExceptSelfs.iterator();
            while (iterator_userExceptSelf.hasNext()) {
                Project_RoleToUser user = iterator_userExceptSelf.next();
                //把已经完成的删掉 只留下未开始和正在进行的
                if (user.getUid().equals(self.getUid()))
                    iterator_userExceptSelf.remove();
            }

            for (Project_RoleToUser userExceptSelf:userExceptSelfs){
                Evaluation_Mutual emutual = new Evaluation_Mutual();
                emutual.setProjectid(projectid);
                emutual.setUid(self.getUid());
                emutual.setTouid(userExceptSelf.getUid());
                emutual.setEvaluated(false);
                evaluation_mutualDao.save(emutual);
            }

        }

        //教师给所有小组成员打分表
        Project_RoleToUser teacher = project_roleToUserDao.findTeacher(projectid);
        //单独给教师的互评赋值,列表已经删除了教师,所以给其他所有人评分即可
        for (Project_RoleToUser project_roleToUser:project_roleToUsers) {
            Evaluation_Mutual emutual = new Evaluation_Mutual();
            emutual.setProjectid(projectid);
            emutual.setUid(teacher.getUid());
            emutual.setTouid(project_roleToUser.getUid());
            emutual.setEvaluated(false);
            evaluation_mutualDao.save(emutual);
        }

        //教师给小组(项目)打分表
        Evaluation_Team eteam = new Evaluation_Team();
        eteam.setProjectid(projectid);
        eteam.setEvaluated(false);
        evaluation_teamDao.save(eteam);

        return true;
    }

    /**
     * 判断项目是否可以正常结束
     * true  可以正常结束
     * false 还有任务或者评分没有完成
     */
    public boolean checkNormallyFinishProject(int projectid){
        //判断是否所有任务结束
        List<Project_Task> project_tasks = project_taskDao.findByProjectTaskpk_Projectid(projectid);
        for (Project_Task project_task:project_tasks)
            if (project_task.getStatus()!=2)
                return false;

        //判断是否所有任务级别评价结束
        List<Evaluation_Member> evaluation_members = evaluation_memberDao.findAllByProjectid(projectid);
        for (Evaluation_Member evaluation_member:evaluation_members)
            if (!evaluation_member.isSelfEvaluated() || !evaluation_member.isPmEvaluated() )
                return false;

        //判断是否所有项目级评价结束
        List<Evaluation_Mutual> evaluation_mutuals = evaluation_mutualDao.findAllByProjectid(projectid);
        for (Evaluation_Mutual evaluation_mutual:evaluation_mutuals)
            if (!evaluation_mutual.isEvaluated())
                return false;

        //判断教师对小组的评价是否结束
        Evaluation_Team evaluation_team = evaluation_teamDao.findAllByProjectid(projectid);
        if (!evaluation_team.isEvaluated())
            return false;


        return true;
    }

    //结束项目

    /**
     * 结束项目
     * true  正常结束
     * false 强行结束
     */
    public boolean finishProject(int projectid){
        boolean flag = checkNormallyFinishProject(projectid);
        Project project = projectDao.findAllByProjectid(projectid);

        if (flag){
            project.setFinishtime(new Date());
            project.setStatus(3);
            projectDao.save(project);
            return true;
        }else {
            project.setFinishtime(new Date());
            project.setStatus(0);
            projectDao.save(project);
            return false;
        }
    }



    public boolean evaluateMutual(List<Evaluation_Mutual> evaluation_mutuals){
        for (Evaluation_Mutual evaluation_mutual:evaluation_mutuals){
            String pmuid = project_roleToUserDao.findPM(evaluation_mutual.getProjectid());
            //如果互评是给pm的评价
            if (evaluation_mutual.getTouid().equals(pmuid)){
                Evaluation_Mutual olde = evaluation_mutualDao.findAllByProjectidAndUidAndTouid(
                        evaluation_mutual.getProjectid(),evaluation_mutual.getUid(),evaluation_mutual.getTouid());
                if (olde.isEvaluated())
                    return false;
                else {
                    olde.setAttitude(evaluation_mutual.getAttitude());
                    olde.setTechnique(evaluation_mutual.getTechnique());
                    olde.setCommunication(evaluation_mutual.getCommunication());
                    olde.setCooperation(evaluation_mutual.getCooperation());
                    olde.setOrganization(evaluation_mutual.getOrganization());
                    olde.setDecision(evaluation_mutual.getDecision());
                    olde.setHelpme(evaluation_mutual.getHelpme());
                    olde.setScore(EvaluateMutual_StudenttoPM(evaluation_mutual));
                    olde.setMark(evaluation_mutual.getMark());
                    olde.setEvaluated(true);
                    evaluation_mutualDao.save(olde);
                }
            }
            //如果互拼是给普通学生的评价
            else{
                Evaluation_Mutual olde = evaluation_mutualDao.findAllByProjectidAndUidAndTouid(
                        evaluation_mutual.getProjectid(),evaluation_mutual.getUid(),evaluation_mutual.getTouid());
                if (olde.isEvaluated())
                    return false;
                else {
                    olde.setAttitude(evaluation_mutual.getAttitude());
                    olde.setTechnique(evaluation_mutual.getTechnique());
                    olde.setCommunication(evaluation_mutual.getCommunication());
                    olde.setCooperation(evaluation_mutual.getCooperation());
                    olde.setHelpme(evaluation_mutual.getHelpme());
                    olde.setScore(EvaluateMutual_StudenttoStudent(evaluation_mutual));
                    olde.setMark(evaluation_mutual.getMark());
                    olde.setEvaluated(true);
                    evaluation_mutualDao.save(olde);
                }
            }
        }
        return false;

    }

    public boolean evaluateTeam(Evaluation_Team evaluation_team){
        Evaluation_Team olde = evaluation_teamDao.findAllByProjectid(evaluation_team.getProjectid());
        if (olde.isEvaluated())
            return false;
        else {
            olde.setDocPassTime(evaluation_team.getDocPassTime());
            olde.setDocCorrectness(evaluation_team.getDocCorrectness());
            olde.setDocInnovation(evaluation_team.getDocInnovation());
            olde.setDocStyle(evaluation_team.getDocStyle());
            olde.setAttitude(evaluation_team.getAttitude());
            olde.setTechnique(evaluation_team.getTechnique());
            olde.setCommunication(evaluation_team.getCommunication());
            olde.setCooperation(evaluation_team.getCooperation());
            olde.setScore(EvaluateTeam(evaluation_team));
            olde.setMark(evaluation_team.getMark());
            olde.setEvaluated(true);
            evaluation_teamDao.save(olde);
            return true;
        }
    }

    public double EvaluateMutual_StudenttoPM(Evaluation_Mutual emutual){
        double score;
        score = emutual.getAttitude()*0.10
                +emutual.getTechnique()*0.16
                +emutual.getCommunication()*0.16
                +emutual.getCooperation()*0.16
                +emutual.getOrganization()*0.16
                +emutual.getDecision()*0.16
                +emutual.getHelpme()*0.10;
        return score;
    }
    public double EvaluateMutual_StudenttoStudent(Evaluation_Mutual emutual){
        double score;
        score = emutual.getAttitude()*0.20
                +emutual.getTechnique()*0.20
                +emutual.getCommunication()*0.20
                +emutual.getCooperation()*0.20
                +emutual.getHelpme()*0.20;
        return score;
    }
    public double EvaluateTeam(Evaluation_Team eteam){
        double score;
        score = eteam.getDocPassTime()*0.10
                +eteam.getDocCorrectness()*0.30
                +eteam.getDocInnovation()*0.10
                +eteam.getDocStyle()*0.10
                +eteam.getAttitude()*0.10
                +eteam.getTechnique()*0.10
                +eteam.getCommunication()*0.10
                +eteam.getCooperation()*0.10;
        return score;
    }



    public List<Evaluation_Mutual> showEvaluateMutualList(int projectid,String uid){
        List<Evaluation_Mutual> evaluation_mutuals = evaluation_mutualDao.findAllByProjectidAndUidAndEvaluated(projectid, uid,false);
        for (Evaluation_Mutual evaluation_mutual:evaluation_mutuals){
            evaluation_mutual.setTousername(userInfoDao.findByUid(evaluation_mutual.getTouid()).getName());
            int toroleid = project_roleToUserDao.findAllByUidAndProjectid(evaluation_mutual.getTouid(),evaluation_mutual.getProjectid()).getRoleid();
            evaluation_mutual.setToroleid(toroleid);
            evaluation_mutual.setTorolename(project_roleDao.findAllByProjectidAndRoleid(evaluation_mutual.getProjectid(),toroleid).getRolename());
        }
        return evaluation_mutuals;
    }

    public String showSumEvaluation(int projectid,String uid){
        Project_RoleToUser teacher = project_roleToUserDao.findTeacher(projectid);
        //所有任务的自评求和
        JSONObject jsonObject = new JSONObject();

        double sumSelfScore = 0;
        int sumSelfCount = 0;
        List<Evaluation_Member> sumTasksSelfEvaluation = evaluation_memberDao.findAllByProjectidAndUidAndSelfEvaluated(projectid,uid,true);
        for (Evaluation_Member evaluation_member:sumTasksSelfEvaluation){
            sumSelfCount++;
            sumSelfScore+=evaluation_member.getSelfScore();
        }
        if (sumSelfCount!=0) {
            sumSelfScore = sumSelfScore / sumSelfCount;
            jsonObject.put("selfScore",new JSONObject(sumSelfScore));
        }
        else
            jsonObject.put("selfScore","暂无评价");


        //所有任务的PM评求和
        String pmuid = project_roleToUserDao.findPM(projectid);
        if (!pmuid.equals(uid)) {
            double sumPmScore = 0;
            int sumPmcount = 0;
            List<Evaluation_Member> sumTasksPmEvaluation = evaluation_memberDao.findAllByProjectidAndUidAndPmEvaluated(projectid, uid, true);
            for (Evaluation_Member evaluation_member : sumTasksPmEvaluation) {
                sumPmcount++;
                sumPmScore += evaluation_member.getPmScore();
            }
            if (sumPmcount != 0) {
                sumPmScore = sumPmScore / sumPmcount;
                jsonObject.put("pmScore",new JSONObject(sumPmScore));
            }
            else
                jsonObject.put("pmScore","暂无评价");
        }else {
            jsonObject.put("pmScore","暂无评价");
        }

        //所有学生互评求和
        double sumMutualStudentScore = 0;
        int sumMutualStudentCount = 0;
        //这里uidnot即把教师排除掉
        List<Evaluation_Mutual> sumMutualStudentEvaluation = evaluation_mutualDao.findAllByProjectidAndUidNotAndTouidAndEvaluated(projectid, teacher.getUid(),uid, true);
        for (Evaluation_Mutual evaluation_mutual:sumMutualStudentEvaluation){
            sumMutualStudentCount++;
            sumMutualStudentScore += evaluation_mutual.getScore();
        }
        if (sumMutualStudentCount != 0) {
            sumMutualStudentScore = sumMutualStudentScore / sumMutualStudentCount;
            jsonObject.put("mutualStudentScore",new JSONObject(sumMutualStudentScore));
        }
        else
            jsonObject.put("mutualStudentScore","暂无评价");

        //教师对所有人的评价
        double sumMutualTeacherScore = 0;
        int sumMutualTeacherCount = 0;
        List<Evaluation_Mutual> sumMutualTeacherEvaluation = evaluation_mutualDao.findAllByProjectidAndUidAndTouidAndEvaluated(projectid, teacher.getUid(),uid, true);
        for (Evaluation_Mutual evaluation_mutual:sumMutualTeacherEvaluation){
            sumMutualTeacherCount++;
            sumMutualTeacherScore += evaluation_mutual.getScore();
        }
        if (sumMutualTeacherCount != 0) {
            sumMutualTeacherScore = sumMutualTeacherScore / sumMutualTeacherCount;
            jsonObject.put("mutualTeacherScore",new JSONObject(sumMutualTeacherScore));
        }
        else
            jsonObject.put("mutualTeacherScore","暂无评价");

        //教师对小组的评价
        Evaluation_Team evaluation_team = evaluation_teamDao.findAllByProjectid(projectid);
        if (evaluation_team.isEvaluated())
            jsonObject.put("teamScore",new JSONObject(evaluation_team.getScore()));
        else
            jsonObject.put("teamScore","暂无评价");


        return jsonObject.toString();
    }


    //AOE图赋值相关操作

    //查找符合的数据在数组中的下标
    public int LocateVer(ALGraph G, Project_Task_pk project_task_pk) {
        int i;
        for (i = 0; i < G.getVernum(); i++)
            if (project_task_pk.equals(G.getVertices().get(i).getProject_task_pk())) return i;

        System.out.println("Can't find!\n");
        return 0;
    }

    //常见图的邻接矩阵
    public ALGraph CreateALGraph(ALGraph G, int projectid) {
        ArcNode arcNode;
        System.out.println("输入顶点数和弧数: ");

        List<Project_Task> project_tasks = project_taskDao.findByProjectTaskpk_Projectid(projectid);
        G.setVernum(project_tasks.size());

        int arcnum = project_taskToTaskDao.countarcnum(projectid);
        G.setArcnum(arcnum);
        List<Project_TaskToTask> project_taskToTasks = project_taskToTaskDao.findAllMidByProjectid(projectid);

        System.out.println("请输入顶点!");
        for (int i = 0; i < G.getVernum(); i++) {
            System.out.println("请输入第" + i + "个顶点: ");
            VNode vNode = new VNode();
            vNode.setProject_task_pk(project_tasks.get(i).getProjectTaskpk());
            vNode.setFirstarc(null);
            vNode.setIndegree(0);
            G.getVertices().add(vNode);
        }
        for (int k = 0; k < G.getArcnum(); k++) {
            int pkPindex, pkSindex;
            System.out.println("请输入弧的顶点和相应权值(v1, v2, w):");
            if (project_taskToTasks.get(k).getPredecessorid() != 0 && project_taskToTasks.get(k).getSuccessorid() != 0) {
                Project_Task_pk project_taskpkP = new Project_Task_pk(projectid, project_taskToTasks.get(k).getPredecessorid());
                Project_Task_pk project_taskpkS = new Project_Task_pk(projectid, project_taskToTasks.get(k).getSuccessorid());
                pkPindex = LocateVer(G, project_taskpkP);
                pkSindex = LocateVer(G, project_taskpkS);
                arcNode = new ArcNode(pkSindex, G.getVertices().get(pkPindex).getFirstarc(), project_taskDao.findByProjectTaskpk(project_taskpkP).getDuration());
                G.getVertices().get(pkPindex).setFirstarc(arcNode);
                G.getVertices().get(pkSindex).setIndegree(G.getVertices().get(pkSindex).getIndegree() + 1);//vi->vj, vj入度加1
            }
        }
        return G;
    }

    public void CriticalPath(ALGraph G) {
        int k, e, l;
        List<Integer> Ve, Vl;
        ArcNode arcNode;
        //以下是求时间最早发生时间
        Ve = new ArrayList<Integer>(G.getVernum());
        Vl = new ArrayList<Integer>(G.getVernum());
//        Vl=new int[G.vernum];
        for (int i = 0; i < G.getVernum(); i++)              //前推
            Ve.add(i, 0);
        for (int i = 0; i < G.getVernum(); i++) {
            ArcNode arcNode1 = G.getVertices().get(i).getFirstarc();
            while (arcNode1 != null) {
                k = arcNode1.getAdjvex();
                if ((Ve.get(i) + arcNode1.getDuration()) > Ve.get(k))
                    Ve.set(k, Ve.get(i) + arcNode1.getDuration());
                arcNode1 = arcNode1.getNextarc();
            }
        }
        //以下是求最迟发生时间
        for (int i = 0; i < G.getVernum(); i++)
            Vl.add(i, Ve.get(G.getVernum() - 1));
//            Vl.set(i,Ve.get(G.getVernum()-1));
        for (int i = G.getVernum() - 2; i >= 0; i--) {     //后推
            arcNode = G.getVertices().get(i).getFirstarc();
            while (arcNode != null) {
                k = arcNode.getAdjvex();
                if ((Vl.get(k) - arcNode.getDuration()) < Vl.get(i))
                    Vl.set(i, Vl.get(k) - arcNode.getDuration());
                arcNode = arcNode.getNextarc();
            }
        }

        for (int i = 0; i < G.getVernum(); i++) {
            arcNode = G.getVertices().get(i).getFirstarc();
            while (arcNode != null) {
                k = arcNode.getAdjvex();
                e = Ve.get(i);    //最早开始时间为时间vi的最早发生时间
                l = Vl.get(k) - arcNode.getDuration();             //最迟开始时间
                Project_Task_pk Ppk = G.getVertices().get(i).getProject_task_pk();
                Project_Task_pk Spk = G.getVertices().get(k).getProject_task_pk();
                char tag; //关键活动
                Project_Task project_task = project_taskDao.findByProjectTaskpk(Ppk);
                if (e == l) {
                    project_task.setIscritical(true);
                    project_task.setEarlystart(e);
                    project_task.setLatestart(l);
                    tag = '*';
                } else {
                    if (!project_task.isIscritical()) {
                        tag = ' ';
                        project_task.setEarlystart(e);
                        project_task.setLatestart(l);
                    } else
                        tag = '*';

                }

                project_taskDao.save(project_task);
                //输出测试查看
                System.out.print(Ppk.getProjectid() + "-" + Ppk.getTaskid() + "," + Spk.getProjectid() + "-" + Spk.getTaskid() + ",");
                System.out.println(e + "," + l + "," + tag);
                arcNode = arcNode.getNextarc();
            }
        }
    }



    public void uploadDOC(int projectid, MultipartFile file) {
        Project project = projectDao.findAllByProjectid(projectid);
        String filePath = project.getFoldername() + "/DOCS";

        try {
            new ProjectFileUtil().uploadDOCSFiles(file.getBytes(), filePath, file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPM(int projectid,String uid){
        return (uid.equals(project_roleToUserDao.findPM(projectid)));
    }

}
