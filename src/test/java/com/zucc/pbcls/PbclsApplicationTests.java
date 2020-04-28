package com.zucc.pbcls;


import com.zucc.pbcls.dao.Project.*;
import com.zucc.pbcls.pojo.AOE.ALGraph;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Case.Case_Role;
import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.*;
import com.zucc.pbcls.service.Admin_CaseService;
import com.zucc.pbcls.service.Admin_UserService;
import com.zucc.pbcls.service.Case.CaseService;
import com.zucc.pbcls.service.Case.Case_RoleService;
import com.zucc.pbcls.service.Case.Case_TaskService;
import com.zucc.pbcls.service.Project.ProjectService;
import com.zucc.pbcls.service.ProjectTaskScheduleService;
import com.zucc.pbcls.service.UserInfoService;
import com.zucc.pbcls.utils.ProjectFileUtil;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
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
		Date olddate = new Date();
		projectService.createProjectByCase("yzl",1,"test1");
		Date newdate = new Date();
		System.out.println(newdate.getTime()-olddate.getTime());
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
		Date olddate = new Date();
		System.out.println(projectService.startProject(1));
		Date newdate = new Date();
		System.out.println(newdate.getTime()-olddate.getTime());
	}

	@Test
	public void testgetprojectfilelist(){
		Project project = projectService.findByProjectid(1);
		new ProjectFileUtil().getProjectFileList(project);
	}
	@Autowired
	Project_TaskOutputDao project_taskOutputDao;
	@Test
	public void getTaskFileList(){
		Project_TaskOutput project = project_taskOutputDao.findAllByProjectidAndTaskid(3,1);
		new ProjectFileUtil().getTaskFileList(project);
	}



	@Autowired
	Admin_CaseService admin_caseService;
	@Test
	public void deleteProject(){
		Date olddate = new Date();
		System.out.println(admin_caseService.deleteProject(3));
		Date newdate = new Date();
		System.out.println(newdate.getTime()-olddate.getTime());
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

//	public class Login {
//		@Rule
//		public JunitPerfRule junitPerfRule = new JunitPerfRule();
//
//		@JunitPerfConfig(duration = 10)
//		public void login() throws InterruptedException {
////			projectService.createProjectByCase("yzl",1,"Performance Testing");
//			projectService.findAllProjects();
//		}
//
//	}

	//引入 ContiPerf 进行性能测试
	@Rule
	public ContiPerfRule rule = new ContiPerfRule();


	//10个线程 执行100次
	@Test
	@PerfTest(invocations = 100,threads = 10)
	public void testCreateProject(){
		projectService.createProjectByCase("yzl",1,"Performance Testing");
	}



	@Test
	@PerfTest(invocations = 100,threads = 10)
	public void testApplyProject(){
		projectService.applyProject("zk",106,2);
	}


	static int projectid = 6;

	@Test
	@PerfTest(invocations = 100,threads = 1)
	public void setRoletoUser(){
		List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAll();
		for (Project_RoleToUser project_roleToUser :project_roleToUsers){
			if (project_roleToUser.getProjectid()==projectid){
				Project_RoleToUser project_roleToUser1 = new Project_RoleToUser();
				project_roleToUser1.setProjectid(project_roleToUser.getProjectid());
				project_roleToUser1.setRoleid(2);
				project_roleToUser1.setUid("zk");
				project_roleToUserDao.save(project_roleToUser1);
				Project_RoleToUser project_roleToUser2 = new Project_RoleToUser();
				project_roleToUser2.setProjectid(project_roleToUser.getProjectid());
				project_roleToUser2.setRoleid(3);
				project_roleToUser2.setUid("yzf");
				project_roleToUserDao.save(project_roleToUser2);
				Project_RoleToUser project_roleToUser3 = new Project_RoleToUser();
				project_roleToUser3.setProjectid(project_roleToUser.getProjectid());
				project_roleToUser3.setRoleid(12);
				project_roleToUser3.setUid("teacher");
				project_roleToUserDao.save(project_roleToUser3);
				projectid++;
			}
		}
	}

	@Test
	@PerfTest(invocations = 100,threads = 1)
	public void testStartPeoject(){
		projectService.startProject(projectid);
		projectid++;
	}

	@Test
	public void deleteRoletoUser(){
		List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAll();
		for (Project_RoleToUser project_roleToUser :project_roleToUsers){
			if (project_roleToUser.getProjectid()>=6 ){
				int roleid = project_roleToUser.getRoleid();
				if (roleid== 12||roleid==2||roleid==3)
					project_roleToUserDao.delete(project_roleToUser);
			}
		}
	}

	@Test
	@PerfTest(invocations = 100,threads = 10)
	public void testFindProjects(){
		Pageable pageable = PageRequest.of(0, 100, Sort.Direction.ASC, "projectname");
		admin_caseService.findProjects(true,true,true,true,"case",pageable);
	}

	@Test
	@PerfTest(invocations = 1000,threads = 10)
	public void testFindUser(){
		Pageable pageable = PageRequest.of(0, 100, Sort.Direction.ASC, "registerTime");
		System.out.println(admin_userService.findUsers(true, true, true,
				"y", true, true, "STUDENT,ADMIN", pageable).getTotalElements());
	}

	static int pagenum = 0;
	@Test
	@PerfTest(invocations = 20,threads = 1)
	public void testShowProjectLog(){
		Pageable pageable = PageRequest.of(pagenum, 30, Sort.Direction.DESC, "logtime");
		System.out.println(projectService.showProjectLog("yzl", pageable));
		pagenum++;
	}

	@Test
	@PerfTest(invocations = 1000,threads = 10)
	public void testShowEvaluateMutualList(){
		projectService.showEvaluateMutualList(1,"yzl");
	}

	@Test
	@PerfTest(invocations = 1000,threads = 10)
	public void testShowSumEvaluation(){
		projectService.showSumEvaluation(1,"yzl");
	}

	@Autowired
	UserInfoService userInfoService;
	@Test
	@PerfTest(invocations = 1000,threads = 10)
	public void testShowUserDetial(){
		userInfoService.showUserDetail("yzl");
	}



//	@Test
//	@PerfTest(invocations = 1,threads = 1)
//	public void testShowProjectinfo(){
//		projectService.;
//	}



}
