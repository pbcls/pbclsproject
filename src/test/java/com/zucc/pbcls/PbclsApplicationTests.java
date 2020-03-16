package com.zucc.pbcls;


import com.zucc.pbcls.dao.Project.Project_RoleDao;
import com.zucc.pbcls.dao.Project.Project_RoleToUserDao;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Case.Case_Role;
import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.pojo.Project.Project_Role;
import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import com.zucc.pbcls.service.Admin_UserService;
import com.zucc.pbcls.service.Case.CaseService;
import com.zucc.pbcls.service.Case.Case_RoleService;
import com.zucc.pbcls.service.Case.Case_TaskService;
import com.zucc.pbcls.service.Project.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

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
		projectService.createProjectByCase("yzl",1,"testproject1");
	}

	@Test
	public void findAllProjects(){
		for (Project project:projectService.findAllProjects()){
			System.out.println(project);
		}
	}

	@Test
	public void findAllByUid(){
		System.out.println(projectService.findAllByUid("yzl"));
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

}
