package com.zucc.pbcls.config;

import com.zucc.pbcls.dao.UserDao;
import com.zucc.pbcls.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class myUserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    public UserDetails loadUserByUsername(String uid) throws AuthenticationException {

        //从数据库根据用户名获取用户信息
        System.out.println(uid);
        User userByName = userDao.findByUid(uid);
        //创建一个新的UserDetails对象，最后验证登陆的需要
        UserDetails userDetails;
        if (userByName == null)
            throw new BadCredentialsException("帐号不存在，请重新输入");
        if(!userByName.isAccountNonLocked())
            throw new BadCredentialsException("帐号被冻结，请联系管理员");


            System.out.println(userByName.getPwd());
            //创建一个集合来存放权限
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+userByName.getRole()));
            //实例化UserDetails对象
            userDetails=new org.springframework.security.core.userdetails.User(uid,userByName.getPwd(),true,true,true,userByName.isAccountNonLocked(), authorities);

        return userDetails;
    }


}
