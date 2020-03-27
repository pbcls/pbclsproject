package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.LogDao;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.service.Project.ProjectService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
public class ProjectTaskScheduleService {
    private static ProjectDao projectDao;
    private static Project_RoleDao project_roleDao;
    private static Project_TaskDao project_taskDao;
    private static Project_RoleToUserDao project_roleToUserDao;
    private static Project_TaskToRoleDao project_taskToRoleDao;
    private static Project_TaskToTaskDao project_taskToTaskDao;
    private static LogDao logDao;
    public static int timeUseMinute = 60 * 1000;
    public static int timeUseDay = 24 * 60 * 60 * 1000;



    @Async
    @Scheduled(cron="0 0/1 * * * ?")//每隔1分钟执行该方法
    //每隔1分钟检查是否有任务超时
    public void checkOverTimeEveryMin(){
        System.out.println(new Date()+":该时间执行了每分钟检查进行中的项目是否有任务超时");
        checkOverTime();
    }

    @Async
    @Scheduled(cron="0 0 0 * * ?")//每天00.00分执行该方法
    //每天0点判断前驱任务已经结束后继任务没有到最早开始时间的任务是否需要开始
    public void AutoStartEveryDay(){
        System.out.println(new Date()+":该时间执行了将可开启的任务开启,将当日为最晚任务开始时间的任务开启");
        try {
            StartTask();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void checkOverTime(){
        int flag = 0;
        //项目status2为正在进行的项目
        List<Project> projects = projectDao.findAllByStatus(2);
        for (Project project:projects){
            //Project.Status为1表示任务正在进行
            List<Project_Task> project_running_tasks = project_taskDao.findByProjectTaskpk_ProjectidAndStatus(project.getProjectid(),1);
            for (Project_Task project_running_task:project_running_tasks){
                int duration=project_running_task.getDuration();
                Date nowdate = new Date();
                Date finishdate = new Date(project_running_task.getStarttime().getTime()+duration*timeUseMinute);
                //现在按照持续时间来判 超过持续时间即超时
                if(nowdate.after(finishdate)){
                    //有任务超时了,将其强行终止,并设置一个flag,以便判断是否要去检查开启后继任务
                    flag = 1;
                    project_running_task.setStatus(2);//设置任务结束
                    project_running_task.setFinishtime(new Date());
                    project_taskDao.save(project_running_task);
                    System.out.println("项目"+project_running_task.getProjectTaskpk().getProjectid()+"任务"+project_running_task.getProjectTaskpk().getTaskid()+"超时,自动结束");

                    /**
                     * 这里还需要写任务评分的逻辑
                     */
                }
            }
            if (flag==1) {
                try {
                    StartTask();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void StartTask() throws ParseException {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String s = sdf.format(new Date());
//        Date date =  sdf.parse(s);
        //项目status2为正在进行的项目
        List<Project> projects = projectDao.findAllByStatus(2);
        //canStart,将可以开始的任务置为开始
        for (Project project:projects){
            //查找未开始且不能开始的任务
            List<Project_Task> projects_unstart_tasks = project_taskDao.findByProjectTaskpk_ProjectidAndStatusAndCanstart(project.getProjectid(),0,false);
            for (Project_Task projects_unstart_task:projects_unstart_tasks){
//                Date earlydate = new Date(dayFormat.parse(dayFormat.format(new Date(project.getStarttime().getTime()))).getTime()+projects_unstart_task.getEarlystart()*timeUseMinute);
//                Date nowday = dayFormat.parse(dayFormat.format(new Date()));
                Date earlydate = new Date(project.getStarttime().getTime()+projects_unstart_task.getEarlystart()*timeUseMinute);
                Date nowday = new Date();
                if (nowday.before(earlydate))
                    continue;//先判断有没有到最早开始时间,没有直接看下一个任务
                else{
                    int flag = 0;
                    //else中判断到了最早开始时间的前驱任务是否全部结束
                    List<Project_TaskToTask> project_taskToTasks_pretasks = project_taskToTaskDao.findAllByProjectidAndSuccessorid(
                            projects_unstart_task.getProjectTaskpk().getProjectid(),projects_unstart_task.getProjectTaskpk().getTaskid());
                    for (Project_TaskToTask project_taskToTasks_pretask:project_taskToTasks_pretasks){
                        //如果为起始点,直接为可以开始
                        if (project_taskToTasks_pretask.getSuccessorid() == 0)
                            break;
                        else {
                            //不为起始点,这里是preid是因为根据后继任务去找任务关系表的信息,然后去找对应得的前驱任务的相应信息
                            Project_Task project_pretask = project_taskDao.findByProjectTaskpk(
                                    new Project_Task_pk(project_taskToTasks_pretask.getProjectid(), project_taskToTasks_pretask.getPredecessorid()));
                            //不等于2即有前驱任务未结束,flag设置为1
                            if (project_pretask.getStatus() != 2) {
                                flag = 1;
                                break;
                            }
                        }
                    }
                    if (flag == 1)
                        continue;
                    else{
                        //能进到这里说明既满足已经过了最早开始时间,又满足前驱任务已经全部完成,或者为起始任务
                        projects_unstart_task.setCanstart(true);
                        project_taskDao.save(projects_unstart_task);
                        System.out.println("项目"+projects_unstart_task.getProjectTaskpk().getProjectid()+"任务"+projects_unstart_task.getProjectTaskpk().getTaskid()+"被设置为canstart");
                    }
                }
            }
        }

        //项目status2为正在进行的项目
        projects = projectDao.findAllByStatus(2);
        //开启canstart的并当天为最晚开始时间的任务
        for(Project project:projects){
            List<Project_Task> projects_canstart_tasks = project_taskDao.findByProjectTaskpk_ProjectidAndStatusAndCanstart(project.getProjectid(),0,true);
            for (Project_Task projects_canstart_task:projects_canstart_tasks) {
//                Date latedate = new Date(dayFormat.parse(dayFormat.format(new Date(project.getStarttime().getTime()))).getTime() + projects_canstart_task.getLatestart() * timeUseMinute);
//                Date nowday = dayFormat.parse(dayFormat.format(new Date()));
                Date latedate = new Date(new Date(project.getStarttime().getTime()).getTime() + projects_canstart_task.getLatestart() * timeUseMinute);
                Date nowday = new Date();
                System.out.println(latedate);
                System.out.println(nowday);
                if (nowday.before(latedate))//如果当天不为最晚开始时间
                    continue;
                else {
                    //自动开启任务
                    projects_canstart_task.setStatus(1);
                    projects_canstart_task.setStarttime(new Date());
                    projects_canstart_task.setNeedcheck(false);
                    project_taskDao.save(projects_canstart_task);
                    System.out.println("项目"+projects_canstart_task.getProjectTaskpk().getProjectid()+"任务"+projects_canstart_task.getProjectTaskpk().getTaskid()+"被开始");
                    //给所有参与任务人员发消息
                    Project_TaskToRole project_taskToRole = project_taskToRoleDao.findAllByProjectidAndTaskid(projects_canstart_task.getProjectTaskpk().getProjectid(),
                            projects_canstart_task.getProjectTaskpk().getTaskid());
                    List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectidAndRoleid(projects_canstart_task.getProjectTaskpk().getProjectid(),
                            project_taskToRole.getRoleid());
                    for (Project_RoleToUser project_roleToUser:project_roleToUsers){
                        Log log = new Log();
                        log.setType(3);
                        log.setProjectid(projects_canstart_task.getProjectTaskpk().getProjectid());
                        log.setTaskid(projects_canstart_task.getProjectTaskpk().getTaskid());
                        log.setTouid(project_roleToUser.getUid());
                        logDao.save(log);
                    }
                }
            }
        }
    }

    public void StartTaskByProjectid(int projectid) throws ParseException {
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        Project project = projectDao.findAllByProjectid(projectid);
        //查找未开始且不能开始的任务
        List<Project_Task> projects_unstart_tasks = project_taskDao.findByProjectTaskpk_ProjectidAndStatusAndCanstart(project.getProjectid(),0,false);
        for (Project_Task projects_unstart_task:projects_unstart_tasks){
//            Date earlydate = new Date(dayFormat.parse(dayFormat.format(new Date(project.getStarttime().getTime()))).getTime()+projects_unstart_task.getEarlystart()*timeUseMinute);
//            Date nowday = dayFormat.parse(dayFormat.format(new Date()));
            Date earlydate = new Date(project.getStarttime().getTime()+projects_unstart_task.getEarlystart()*timeUseMinute);
            Date nowday = new Date();
            if (nowday.before(earlydate))
                continue;//先判断有没有到最早开始时间,没有直接看下一个任务
            else{
                int flag = 0;
                //else中判断到了最早开始时间的前驱任务是否全部结束
                List<Project_TaskToTask> project_taskToTasks_pretasks = project_taskToTaskDao.findAllByProjectidAndSuccessorid(
                        projects_unstart_task.getProjectTaskpk().getProjectid(),projects_unstart_task.getProjectTaskpk().getTaskid());
                for (Project_TaskToTask project_taskToTasks_pretask:project_taskToTasks_pretasks){
                    //如果为起始点,直接为可以开始
                    if (project_taskToTasks_pretask.getPredecessorid() == 0)
                        break;
                    else {
                        //不为起始点,这里是preid是因为根据后继任务去找任务关系表的信息,然后去找对应得的前驱任务的相应信息
                        Project_Task project_pretask = project_taskDao.findByProjectTaskpk(
                                new Project_Task_pk(project_taskToTasks_pretask.getProjectid(), project_taskToTasks_pretask.getPredecessorid()));
                        //不等于2即有前驱任务未结束,flag设置为1
                        if (project_pretask.getStatus() != 2) {
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 1)
                    continue;
                else{
                    //能进到这里说明既满足已经过了最早开始时间,又满足前驱任务已经全部完成,或者为起始任务
                    projects_unstart_task.setCanstart(true);
                    project_taskDao.save(projects_unstart_task);
                    System.out.println("项目"+projects_unstart_task.getProjectTaskpk().getProjectid()+"任务"+projects_unstart_task.getProjectTaskpk().getTaskid()+"被设置为canstart");
                }
            }
        }

        List<Project_Task> projects_canstart_tasks = project_taskDao.findByProjectTaskpk_ProjectidAndStatusAndCanstart(project.getProjectid(),0,true);
        for (Project_Task projects_canstart_task:projects_canstart_tasks) {
//            Date latedate = new Date(dayFormat.parse(dayFormat.format(new Date(project.getStarttime().getTime()))).getTime() + projects_canstart_task.getLatestart() * timeUseMinute);
//            Date nowday = dayFormat.parse(dayFormat.format(new Date()));
            Date latedate = new Date(new Date(project.getStarttime().getTime()).getTime() + projects_canstart_task.getLatestart() * timeUseMinute);
            Date nowday = new Date();
            if (nowday.before(latedate))//如果当天不为最晚开始时间
                continue;
            else {
                //自动开启任务
                projects_canstart_task.setStatus(1);
                projects_canstart_task.setStarttime(new Date());
//                projects_canstart_task.setNeedsubmit(true);
                projects_canstart_task.setNeedcheck(false);
                project_taskDao.save(projects_canstart_task);
                System.out.println("项目"+projects_canstart_task.getProjectTaskpk().getProjectid()+"任务"+projects_canstart_task.getProjectTaskpk().getTaskid()+"被开始");
                //给所有参与任务人员发消息
                Project_TaskToRole project_taskToRole = project_taskToRoleDao.findAllByProjectidAndTaskid(projects_canstart_task.getProjectTaskpk().getProjectid(),
                        projects_canstart_task.getProjectTaskpk().getTaskid());
                List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectidAndRoleid(projects_canstart_task.getProjectTaskpk().getProjectid(),
                        project_taskToRole.getRoleid());
                for (Project_RoleToUser project_roleToUser:project_roleToUsers){
                    Log log = new Log();
                    log.setType(3);
                    log.setProjectid(projects_canstart_task.getProjectTaskpk().getProjectid());
                    log.setTaskid(projects_canstart_task.getProjectTaskpk().getTaskid());
                    log.setTouid(project_roleToUser.getUid());
                    logDao.save(log);
                }
            }
        }
    }


    public static ProjectDao getProjectDao() {
        return projectDao;
    }

    public static void setProjectDao(ProjectDao projectDao) {
        ProjectTaskScheduleService.projectDao = projectDao;
    }

    public static Project_RoleDao getProject_roleDao() {
        return project_roleDao;
    }

    public static void setProject_roleDao(Project_RoleDao project_roleDao) {
        ProjectTaskScheduleService.project_roleDao = project_roleDao;
    }

    public static Project_TaskDao getProject_taskDao() {
        return project_taskDao;
    }

    public static void setProject_taskDao(Project_TaskDao project_taskDao) {
        ProjectTaskScheduleService.project_taskDao = project_taskDao;
    }

    public static Project_RoleToUserDao getProject_roleToUserDao() {
        return project_roleToUserDao;
    }

    public static void setProject_roleToUserDao(Project_RoleToUserDao project_roleToUserDao) {
        ProjectTaskScheduleService.project_roleToUserDao = project_roleToUserDao;
    }

    public static Project_TaskToRoleDao getProject_taskToRoleDao() {
        return project_taskToRoleDao;
    }

    public static void setProject_taskToRoleDao(Project_TaskToRoleDao project_taskToRoleDao) {
        ProjectTaskScheduleService.project_taskToRoleDao = project_taskToRoleDao;
    }

    public static Project_TaskToTaskDao getProject_taskToTaskDao() {
        return project_taskToTaskDao;
    }

    public static void setProject_taskToTaskDao(Project_TaskToTaskDao project_taskToTaskDao) {
        ProjectTaskScheduleService.project_taskToTaskDao = project_taskToTaskDao;
    }
    public static LogDao getLogDao() {
        return logDao;
    }

    public static void setLogDao(LogDao logDao) {
        ProjectTaskScheduleService.logDao = logDao;
    }

}

