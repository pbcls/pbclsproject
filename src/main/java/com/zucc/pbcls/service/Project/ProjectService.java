package com.zucc.pbcls.service.Project;

import com.zucc.pbcls.dao.Case.*;
import com.zucc.pbcls.dao.LogDao;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.AOE.ALGraph;
import com.zucc.pbcls.pojo.AOE.ArcNode;
import com.zucc.pbcls.pojo.AOE.VNode;
import com.zucc.pbcls.pojo.Case.*;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
            project_taskToRole.setProject_role(project_roleDao.findAllByProjectidAndRoleid(project.getProjectid(),case_taskToRole.getCase_role().getRoleid()));
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

    public JSONArray findAllByUid(String uid) {
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByUid(uid);
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
    public List<Log> showProjectLog(String uid) {
        List<Log> logs = logDao.findAllByTouid(uid);
        for (Log log : logs) {
            if (log.getCaseid() != 0)
                log.setCasename(caseDao.findAllByCaseid(log.getCaseid()).getCasename());
            if (log.getProjectid() != 0)
                log.setProjectname(projectDao.findAllByProjectid(log.getProjectid()).getProjectname());
            if (log.getTaskid() != 0)
                log.setTaskname(project_taskDao.findByProjectTaskpk(new Project_Task_pk(log.getProjectid(), log.getTaskid())).getTaskname());
            if (log.getRoleid() != 0)
                log.setRolename(project_roleDao.findAllByProjectidAndRoleid(log.getProjectid(),log.getRoleid()).getRolename());
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
        return true;
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
            new ProjectFileUtil().uploadFiles(file.getBytes(), filePath, file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
