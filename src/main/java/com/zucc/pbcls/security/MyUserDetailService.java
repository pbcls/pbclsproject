package com.zucc.pbcls.security;

import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserInfoDao userInfoDao;

    public UserDetails loadUserByUsername(String uid) throws AuthenticationException {

        //从数据库根据用户名获取用户信息
        System.out.println(uid);
        MyUser myUserByUid = userInfoDao.findByUid(uid);
        //创建一个新的UserDetails对象,使用自己定义的实现类,以便实现扩展的session
        if (myUserByUid == null)
            throw new BadCredentialsException("帐号不存在，请重新输入");
        if (!myUserByUid.isAccountNonLocked())
            throw new BadCredentialsException("帐号被冻结，请联系管理员");


        System.out.println(myUserByUid.getPwd());
        //创建一个集合来存放权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + myUserByUid.getRole()));
        //实例化UserDetails对象
        return new UserInfo(uid, myUserByUid.getName(), myUserByUid.getPortrait(), myUserByUid.getPwd(), true, true, true, myUserByUid.isAccountNonLocked(), authorities);

    }


}
