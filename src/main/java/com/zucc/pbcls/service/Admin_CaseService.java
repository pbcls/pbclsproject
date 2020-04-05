package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.Case.*;
import com.zucc.pbcls.dao.LogDao;
import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.pojo.Case.*;
import com.zucc.pbcls.pojo.Log;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.utils.CaseFileUtil;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class Admin_CaseService {
    @Autowired
    CaseDao caseDao;
    @Autowired
    Case_RoleDao case_roleDao;
    @Autowired
    Case_TaskDao case_taskDao;
    @Autowired
    Case_TaskToRoleDao case_taskToRoleDao;
    @Autowired
    Case_TaskToTaskDao case_taskToTaskDao;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    Project_RoleDao project_roleDao;
    @Autowired
    Project_RoleToUserDao project_roleToUserDao;
    @Autowired
    Project_TaskDao project_taskDao;
    @Autowired
    Project_TaskToTaskDao project_taskToTaskDao;
    @Autowired
    Project_TaskToRoleDao project_taskToRoleDao;
    @Autowired
    Project_TaskOutputDao project_taskOutputDao;
    @Autowired
    LogDao logDao;


    //分页查询
    public Page<CaseInfo> findCases(boolean statuson,boolean statusoff,String findstr,Pageable pageable) {
        Page<CaseInfo> pageList = caseDao.findAll(new Specification<CaseInfo>(){
            @Override
            public Predicate toPredicate(Root<CaseInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> listAndstatus = new ArrayList<Predicate>();
                if (statuson&&!statusoff){
                    listAndstatus.add(cb.equal(root.get("status"),true));
                }else if (!statuson&&statusoff){
                    listAndstatus.add(cb.equal(root.get("status"),false));
                }else if (!statuson&&!statusoff){
                    listAndstatus.add(cb.equal(root.get("status"),3));
                }
                Predicate[] arrayStatus = new Predicate[listAndstatus.size()];
                Predicate Status = cb.and(listAndstatus.toArray(arrayStatus));

                List<Predicate> listName = new ArrayList<Predicate>();
                listName.add(cb.like(root.get("casename"), "%" + findstr + "%"));
                Predicate[] arrayName = new Predicate[listName.size()];
                Predicate Name = cb.and(listName.toArray(arrayName));

                return query.where(Status,Name).getRestriction();
            }
        }, pageable);
        return pageList;
    }


    public Page<Project> findProjects(boolean status0,boolean status1,boolean status2,boolean status3, String findstr, Pageable pageable) {
        Page<Project> pageList = projectDao.findAll(new Specification<Project>(){
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> liststatus = new ArrayList<Predicate>();
                if (status0)
                    liststatus.add(cb.equal(root.get("status"),0));
                if (status1)
                    liststatus.add(cb.equal(root.get("status"),1));
                if (status2)
                    liststatus.add(cb.equal(root.get("status"),2));
                if (status3)
                    liststatus.add(cb.equal(root.get("status"),3));
                Predicate[] arrayStatus = new Predicate[liststatus.size()];
                Predicate Status = cb.or(liststatus.toArray(arrayStatus));


                List<Predicate> listName = new ArrayList<Predicate>();
                listName.add(cb.like(root.get("projectname"), "%" + findstr + "%"));

                Predicate[] arrayName = new Predicate[listName.size()];
                Predicate Name = cb.and(listName.toArray(arrayName));

                return query.where(Status,Name).getRestriction();
            }
        }, pageable);
        return pageList;
    }




    public boolean updateStatus(int caseid , boolean status){
        CaseInfo caseInfo = caseDao.findAllByCaseid(caseid);
        if (caseInfo!=null) {
            caseInfo.setStatus(status);
            caseDao.save(caseInfo);
            return true;
        }else
            return false;//已经被删除
    }

    /**
     * 0  已经被删除
     * 1  下面还有运行的项目
     * 2  成功删除
     */
    public int deleteCase(int caseid){
        CaseInfo caseInfo = caseDao.findAllByCaseid(caseid);
        if (caseInfo!=null){
            List<Project> projects = projectDao.findAllByCaseid(caseid);
            int isunfinish = 0;
            for (Project project:projects){
                if (project.getStatus() == 1 || project.getStatus() == 2)
                    isunfinish = 1;
                    break;
            }

            //开始做删除
            if (isunfinish == 1){
                //1.下面有未完成项目,无法删除,只能修改status使他无法再创建项目
                caseInfo.setStatus(false);
                caseDao.save(caseInfo);
                return 1;
            }else {
                for (Project project:projects){
                    deleteProject(project.getProjectid());
                }
                //删除和案例有关的一切log
                List<Log> logs = logDao.findAllByCaseid(caseid);
                for (Log log:logs){
                    logDao.delete(log);
                }

                List<Case_Role> case_roles = case_roleDao.findAllByCaseid(caseid);
                for (Case_Role case_role:case_roles){
                    case_roleDao.delete(case_role);
                }

                List<Case_Task> case_tasks = case_taskDao.findByCaseTaskpk_Caseid(caseid);
                for (Case_Task case_task:case_tasks){
                    case_taskDao.delete(case_task);
                }

                List<Case_TaskToRole> case_taskToRoles = case_taskToRoleDao.findAllByCaseid(caseid);
                for (Case_TaskToRole case_taskToRole:case_taskToRoles){
                    case_taskToRoleDao.delete(case_taskToRole);
                }

                List<Case_TaskToTask> case_taskToTasks = case_taskToTaskDao.findAllByCaseid(caseid);
                for (Case_TaskToTask case_taskToTask:case_taskToTasks){
                    case_taskToTaskDao.delete(case_taskToTask);
                }

                //删除文件
                File file_case = new File("src/main/resources/static/case"+caseInfo.getFoldername());
                new CaseFileUtil().delCaseFile(file_case);

                caseDao.delete(caseInfo);
                return 2;
            }
        }else
            return 0;
    }


    public boolean deleteProject(int projectid){
        Project project = projectDao.findAllByProjectid(projectid);
        if (project!=null){
            List<Project_Role> project_roles = project_roleDao.findAllByProjectid(projectid);
            for (Project_Role project_role:project_roles){
                project_roleDao.delete(project_role);
            }

            List<Project_Task> project_tasks = project_taskDao.findByProjectTaskpk_Projectid(projectid);
            for (Project_Task project_task:project_tasks){
                project_taskDao.delete(project_task);
            }

            List<Project_TaskToTask> project_taskToTasks = project_taskToTaskDao.findAllByProjectid(projectid);
            for (Project_TaskToTask project_taskToTask:project_taskToTasks){
                project_taskToTaskDao.delete(project_taskToTask);
            }

            List<Project_TaskToRole> project_taskToRoles = project_taskToRoleDao.findAllByProjectid(projectid);
            for (Project_TaskToRole project_taskToRole:project_taskToRoles){
                project_taskToRoleDao.delete(project_taskToRole);
            }
            //注意要删除和项目有关的一切log
            List<Log> logs = logDao.findAllByProjectid(projectid);
            for (Log log:logs){
                logDao.delete(log);
            }

            List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByProjectid(projectid);
            for (Project_RoleToUser project_roleToUser:project_roleToUsers){
                Log log = new Log();
                log.setType(7);
                log.setTouid(project_roleToUser.getUid());
                log.setLogmsg("您所在的"+projectDao.findAllByProjectid(projectid).getProjectname()+"项目已经被管理员删除");
                logDao.save(log);
                project_roleToUserDao.delete(project_roleToUser);
            }


            List<Project_TaskOutput> project_taskOutputs = project_taskOutputDao.findAllByProjectid(projectid);
            for (Project_TaskOutput project_taskOutput:project_taskOutputs){
                project_taskOutputDao.delete(project_taskOutput);
            }

            //删除这个project有关的所有文件
            File file_project = new File("src/main/resources/static/case"+project.getFoldername());
            new ProjectFileUtil().delProjectFile(file_project);


            CaseInfo caseInfo = caseDao.findAllByCaseid(project.getCaseid());
            caseInfo.setInstances(caseInfo.getInstances()-1);
            if (project.getStatus()!=1)//如果项目为除了未开始的情况,即已经结束或正在进行
                caseInfo.setStartedinstances(caseInfo.getStartedinstances()-1);
            if (project.getStatus() == 3 || project.getStatus() == 0)//如果项目已经结束
                caseInfo.setFinishedinstances(caseInfo.getFinishedinstances()-1);

            projectDao.delete(project);

            return true;
        }else
            return false;//项目已经被删除
    }


/**
 * 项目查找
 * 项目删除?
 * 项目列表
 * 等等功能等待项目模块结束后补充
 */
}
