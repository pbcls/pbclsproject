package com.zucc.pbcls.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserInfo extends User {
    private static final long serialVersionUID = 500L;
    private String myUserName;
    private String myUserPortrait;

    @SuppressWarnings("deprecation")
    public UserInfo(String username,String myUserName,String myUserPortrait,  String password, boolean enabled, boolean accountNonExpired,
                    boolean credentialsNonExpired, boolean accountNonLocked,
                    Collection<? extends GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username,password, enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,authorities);
        this.myUserName=myUserName;
        this.myUserPortrait=myUserPortrait;
    }

    public String getMyUserName() {
        return myUserName;
    }

    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
    }

    public String getMyUserPortrait() {
        return myUserPortrait;
    }

    public void setMyUserPortrait(String myUserPortrait) {
        this.myUserPortrait = myUserPortrait;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
