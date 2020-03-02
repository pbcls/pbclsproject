package com.zucc.pbcls;


import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.service.Admin_UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PbclsApplicationTests {

	@Autowired
	Admin_UserService admin_userService;

	@Test
	public void contextLoads() {
		Pageable pageable = PageRequest.of(1, 10, Sort.Direction.ASC, "uid");
		String findstr="f";
		for (MyUser user:admin_userService.findUsers(false,false,true,findstr,
				true,true,"ADMIN,TEACHER,STUDENT")){
			System.out.println(user);
		}

	}

}
