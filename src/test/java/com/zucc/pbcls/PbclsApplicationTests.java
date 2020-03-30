package com.zucc.pbcls;


import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.pojo.AOE.ALGraph;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Case.Case_Role;
import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.service.Admin_UserService;
import com.zucc.pbcls.service.Case.CaseService;
import com.zucc.pbcls.service.Case.Case_RoleService;
import com.zucc.pbcls.service.Case.Case_TaskService;
import com.zucc.pbcls.service.Project.ProjectService;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.zucc.pbcls.service.ProjectTaskScheduleService.timeUseDay;
import static com.zucc.pbcls.service.ProjectTaskScheduleService.timeUseMinute;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PbclsApplicationTests {

	@Autowired
	Admin_UserService admin_userService;
	@Autowired
	Case_TaskService case_taskService;
	@Autowired
	Case_RoleService case_roleService;
	@Autowired
	CaseService caseService;
	@Autowired
	ProjectService projectService;
	@Autowired
	Project_RoleToUserDao project_roleToUserDao;
	@Autowired
	Project_RoleDao project_roleDao;
	@Autowired
	Project_TaskToTaskDao project_taskToTaskDao;
	@Autowired
	Project_TaskDao project_taskDao;
	@Autowired
	ProjectDao projectDao;


	@Test
	public void testfinduser() {
		Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "registerTime");
		String findstr="l";
		Page<MyUser> page = admin_userService.findUsers(true,true,true,findstr,
				true,true,"ADMIN,TEACHER,STUDENT",pageable);
		System.out.println(page.getTotalElements());
		System.out.println(page.getTotalPages());
//		for (MyUser user:admin_userService.findUsers(false,false,true,findstr,
//				true,true,"ADMIN,TEACHER,STUDENT")){
//			System.out.println(user);
//		}

	}

	@Test
	public void testfindByCaseTaskpk_Caseid(){
		for (Case_Task case_task:case_taskService.findByCaseTaskpk_Caseid(1)){
			System.out.println(case_task);
		}
	}

	@Test
	public void testfindAllByCaseTaskpk(){
		System.out.println(case_taskService.findAllByCaseTaskpk(new Case_Task_pk(1,2)));
	}

	@Test
	public void testfindAllCases(){
		for (CaseInfo c:caseService.findAllCases()){
			System.out.println(c);
		}
	}

	@Test
	public void testfindAllByCaseid(){
			System.out.println(caseService.findAllByCaseid(2));
	}


	@Test
	public void testfindAllRoleByCaseid(){
		for (Case_Role cr:case_roleService.findAllRoleByCaseid(2)){
			System.out.println(cr);
		}
	}

	@Test
	public void createProjectByCase(){
		projectService.createProjectByCase("yzl",1,"testDOCSproject1");
	}

	@Test
	public void findAllProjects(){
		for (CaseInfo caseInfo:projectService.findAllProjects()){
			System.out.println(caseInfo);
		}
	}

	@Test
	public void findUnfinishedProjectsByUid(){
		System.out.println(projectService.findUnfinishedProjectsByUid("szz"));
	}


	@Test
	public void findPM(){
		System.out.println(project_roleToUserDao.findPM(1));
	}

	@Test
	public void isAllRoleUsed(){
		System.out.println(project_roleDao.isAllRoleUsed(1));
	}

	@Test
	public void startProject(){
		System.out.println(projectService.startProject(1));
	}

	@Test
	public void eq(){
		Project_Task_pk project_task_pk1 = new Project_Task_pk(1,2);
		Project_Task_pk project_task_pk2 = new Project_Task_pk(1,2);
		if (project_task_pk1==project_task_pk2)
			System.out.println("1");
		else
			System.out.println("2");
	}
	@Test
	public void arcnum(){
		System.out.println(project_taskToTaskDao.countarcnum(1));
	}

	@Test
	public void AOE(){
		ALGraph G = new ALGraph();
		System.out.println("以下是查找图的关键路径的程序。");
		G=projectService.CreateALGraph(G,1);
		projectService.CriticalPath(G);
	}

	@Test
	public void findfirstandlasttask(){
		for (Project_Task project_task:project_taskDao.findAllFirstTasks(1)){
			System.out.println(project_task);
		}
		System.out.println("last");
		System.out.println(project_taskDao.findLastTask(1));

	}

	@Test
	public void findTeacher(){
		System.out.println(project_roleToUserDao.findTeacher(2));
	}

	@Test
	public void delprojectfile(){
		File file_project = new File("src/main/resources/static/case"+projectDao.findAllByProjectid(1).getFoldername());
		new ProjectFileUtil().delProjectFile(file_project);
	}

	@Test
	public void findByProjectidAndUid(){
		for (Project_Task project_task:project_taskDao.findByProjectidAndUid(1,"szz")){
			System.out.println(project_task);
		}
	}

	@Test
	public void Datetest() throws ParseException {
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		Project_Task project_task = project_taskDao.findByProjectTaskpk(new Project_Task_pk(1,1));
		Project project = projectDao.findAllByProjectid(1);
		Date latedate = new Date(new Date(project.getStarttime().getTime()).getTime() + project_task.getLatestart() * timeUseMinute);
		Date nowday = new Date();
		System.out.println(latedate);
		System.out.println(nowday);
	}

	@Test
	public void AutoStartEveryDay() {
//		new ProjectTaskScheduleService().AutoStartEveryDay();
		int a = 5;
//		double b = 1;
//		double c = b/a;
//		System.out.println(c);
		if ("暂无评价".equals(a))
			System.out.println("相等");
		else
			System.out.println("不相等");
	}
}
