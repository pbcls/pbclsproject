package com.zucc.pbcls.config;


import com.zucc.pbcls.Listener.ExcelListener;
import com.zucc.pbcls.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
//该类作用为,将Listener,Filter等不被spring托管的类中的对象注入,以实现该些类中的Autowired注解生效
public class ListenerInitRun implements CommandLineRunner {

    @Autowired
    UserInfoDao userInfoDao;

    @Override
    public void run(String... args) throws Exception {
        //解决listener注入不了spring容器对象的问题
        ExcelListener.setUserInfoDao(userInfoDao);
    }
}

