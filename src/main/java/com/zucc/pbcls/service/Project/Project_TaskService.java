package com.zucc.pbcls.service.Project;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.zucc.pbcls.dao.Evaluation_MemberDao;
import com.zucc.pbcls.dao.Evaluation_MutualDao;
import com.zucc.pbcls.dao.LogDao;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.Case.Case_TaskToTask;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.Evaluation_Member;
import com.zucc.pbcls.pojo.Evaluation_Mutual;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.security.UserInfo;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    Evaluation_MemberDao evaluation_memberDao;
    @Autowired
    Evaluation_MutualDao evaluation_mutualDao;

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



    public String showProjectTaskInfo(int projectid,int taskid,String uid){
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
        //如果任务已提交或完成找对应文件
        List<String> taskOutput = new ArrayList<>();
//        if(project_task.getStatus() == 2 || project_task.isNeedcheck()) {
            Project_TaskOutput project_taskOutput = project_taskOutputDao.findAllByProjectidAndTaskid(projectid, taskid);
            taskOutput = new ProjectFileUtil().getTaskFileList(project_taskOutput);
//        }


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

        MyUser myUser = userInfoDao.findByUid(uid);

        //如果任务完成找到对应评价
        //如果他是这个任务的人
        if(project_task.getStatus() == 2 && users.contains(myUser)){
            Evaluation_Member evaluation_member = evaluation_memberDao.findAllByProjectidAndTaskidAndUid(projectid,taskid,uid);
            JSONObject json_evaluation_member = new JSONObject(evaluation_member);
            jsonObject.put("evaluation",json_evaluation_member);
        }
        //如果他不是这个任务的但是他是PM
        else if (project_task.getStatus() == 2 && uid.equals(project_roleToUserDao.findPM(projectid))){
            Evaluation_Member evaluation_member = evaluation_memberDao.findAllByProjectidAndTaskid(projectid,taskid).get(0);
            JSONObject json_evaluation_member = new JSONObject(evaluation_member);
            jsonObject.put("evaluation",json_evaluation_member);
        }

        return jsonObject.toString();
    }

    public void checkTask(int projectid,int taskid,boolean pass){
        Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(projectid,taskid));
        Project_Task lastTask = project_taskDao.findLastTask(projectid);
        Log log = new Log();//告知文档审核结果的通知
        if (pass){
            project_task.setStatus(2);
            project_task.setFinishtime(new Date());
            project_taskDao.save(project_task);
            System.out.println("项目"+project_task.getProjectTaskpk().getProjectid()+"任务"+project_task.getProjectTaskpk().getTaskid()+"被结束");

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
        //提醒评分
        if(pass){
            for (Project_RoleToUser project_roleToUser:project_roleToUsers){
                //告知任务结束的通知
                Log taskfinishlog = new Log();
                taskfinishlog.setType(4);
                taskfinishlog.setProjectid(projectid);
                taskfinishlog.setTaskid(taskid);
                taskfinishlog.setTouid(project_roleToUser.getUid());
                logDao.save(taskfinishlog);
                //提醒去评分的通知
                Log eLog = new Log();
                eLog.setType(5);
                eLog.setProjectid(projectid);
                eLog.setTaskid(taskid);
                eLog.setTouid(project_roleToUser.getUid());
                logDao.save(eLog);
            }
            /**
             * 这里补充是最后一个任务的评分相关
             */
            if (project_task.getProjectTaskpk()==lastTask.getProjectTaskpk()){
                //提醒项目互评的通知:最后一个任务结束
                List<Project_RoleToUser> projectallusers = project_roleToUserDao.findAllByProjectid(projectid);
                for (Project_RoleToUser user:projectallusers) {
                    Log projectEvaluationLog = new Log();
                    projectEvaluationLog.setType(6);
                    projectEvaluationLog.setProjectid(projectid);
                    projectEvaluationLog.setTouid(user.getUid());
                    logDao.save(projectEvaluationLog);
                }
            }
        }
    }

    //evaluateMember为每项任务后的自评以及pm评价
    public boolean evaluateMember(Evaluation_Member evaluation_member,String submituid){
        //uid为评价表中的uid,即被评价人的uid
        String uid = evaluation_member.getUid();
        String PMuid = project_roleToUserDao.findPM(evaluation_member.getProjectid());
        //提交评价的为项目经理
        if (submituid.equals(PMuid)){
            //如果需要评价的角色为项目经理,评价即为自评加上pm评
            if (submituid.equals(uid)){
                Evaluation_Member olde = evaluation_memberDao.findAllByProjectidAndTaskidAndUid(evaluation_member.getProjectid(),evaluation_member.getTaskid(),evaluation_member.getUid());
                //已经有过自评记录了
                if (olde.isSelfEvaluated()|| olde.isPmEvaluated())
                    return false;
                else {
                    olde.setSelfAttitude(evaluation_member.getSelfAttitude());
                    olde.setSelfTechnique(evaluation_member.getSelfTechnique());
                    olde.setSelfCommunication(evaluation_member.getSelfCommunication());
                    olde.setSelfCooperation(evaluation_member.getSelfCooperation());
                    olde.setSelfAchievement(evaluation_member.getSelfAchievement());
                    olde.setSelfOrganization(evaluation_member.getSelfOrganization());
                    olde.setSelfDecision(evaluation_member.getSelfDecision());
                    olde.setSelfScore(EvaluateMember_PMSelf(evaluation_member));
                    olde.setSelfMark(evaluation_member.getSelfMark());
                    olde.setSelfExpectation(evaluation_member.getSelfExpectation());
                    olde.setSelfEvaluated(true);
                    olde.setPmEvaluated(true);
                    evaluation_memberDao.save(olde);
                    return true;
                }

            }
            //如果需要评价的角色不是项目经理,就是项目经理给别人评价
            else {
                List<Evaluation_Member> oldes = evaluation_memberDao.findAllByProjectidAndTaskid(evaluation_member.getProjectid(),evaluation_member.getTaskid());
                for (Evaluation_Member olde:oldes){
                    if (olde.isPmEvaluated())
                        return false;
                    else {
                        olde.setPmAttitude(evaluation_member.getPmAttitude());
                        olde.setPmTechnique(evaluation_member.getPmTechnique());
                        olde.setPmCommunication(evaluation_member.getPmCommunication());
                        olde.setPmCooperation(evaluation_member.getPmCooperation());
                        olde.setPmDocPassTime(evaluation_member.getPmDocPassTime());
                        olde.setPmDocPassRate(evaluation_member.getPmDocPassRate());
                        olde.setPmDocCorrectness(evaluation_member.getPmDocCorrectness());
                        olde.setPmDocInnovation(evaluation_member.getPmDocInnovation());
                        olde.setPmDocStyle(evaluation_member.getPmDocStyle());
                        olde.setPmScore(EvaluateMember_PMtoStudent(evaluation_member));
                        olde.setPmMark(evaluation_member.getPmMark());
                        olde.setPmEvaluated(true);
                        evaluation_memberDao.save(olde);
                        return true;
                    }
                }
                return false;
            }
        }
        //提交评价的为普通学生,为自评
        else {
            Evaluation_Member olde = evaluation_memberDao.findAllByProjectidAndTaskidAndUid(evaluation_member.getProjectid(),evaluation_member.getTaskid(),evaluation_member.getUid());
            //已经有过自评记录了
            if (olde.isSelfEvaluated())
                return false;
            else {
                olde.setSelfAttitude(evaluation_member.getSelfAttitude());
                olde.setSelfTechnique(evaluation_member.getSelfTechnique());
                olde.setSelfCommunication(evaluation_member.getSelfCommunication());
                olde.setSelfCooperation(evaluation_member.getSelfCooperation());
                olde.setSelfAchievement(evaluation_member.getSelfAchievement());
                olde.setSelfScore(EvaluateMember_StudentSelf(evaluation_member));
                olde.setSelfMark(evaluation_member.getSelfMark());
                olde.setSelfExpectation(evaluation_member.getSelfExpectation());
                olde.setSelfEvaluated(true);
                evaluation_memberDao.save(olde);
                return true;
            }
        }
    }



    public double EvaluateMember_PMtoStudent(Evaluation_Member emember){
        double score;
        score = emember.getPmDocPassRate()*0.05
                +emember.getPmDocPassTime()*0.05
                +emember.getPmDocCorrectness()*0.3
                +emember.getPmDocInnovation()*0.1
                +emember.getPmDocStyle()*0.1
                +emember.getPmAttitude()*0.1
                +emember.getPmTechnique()*0.1
                +emember.getPmCommunication()*0.1
                +emember.getPmCooperation()*0.1;
        return score;

    }
    public double EvaluateMember_StudentSelf(Evaluation_Member emember){
        double score;
        score = emember.getSelfAttitude()*0.2
                +emember.getSelfTechnique()*0.2
                +emember.getSelfCommunication()*0.2
                +emember.getSelfCooperation()*0.2
                +emember.getSelfAchievement()*0.2;
        return score;
    }
    public double EvaluateMember_PMSelf(Evaluation_Member emember){
        double score;
        score = emember.getSelfAttitude()*0.14
                +emember.getSelfTechnique()*0.14
                +emember.getSelfCommunication()*0.14
                +emember.getSelfCooperation()*0.14
                +emember.getSelfAchievement()*0.16
                +emember.getSelfOrganization()*0.14
                +emember.getSelfDecision()*0.14;
        return score;
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
