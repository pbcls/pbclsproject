package com.zucc.pbcls.security;

import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 该方法弃用,前端session.SPRING_SECURITY_LAST_EXCEPTION.message参数通过getMessage的code传值,而不是后面的defaultMessage
 */


public class LoginAuthenticationProvider extends DaoAuthenticationProvider {


    private UserInfoDao userInfoDao;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public LoginAuthenticationProvider(UserDetailsService userDetailsService,UserInfoDao userInfoDao) {
        super();
        // 这个地方一定要对userDetailsService赋值，不然userDetailsService是null
        setUserDetailsService(userDetailsService);
        this.userInfoDao = userInfoDao;
    }



    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }else {
                MyUser myUser = userInfoDao.findByUid(userDetails.getUsername());
                myUser.setLoginNum(myUser.getLoginNum()+1);
                userInfoDao.save(myUser);
            }
        }
    }
}
