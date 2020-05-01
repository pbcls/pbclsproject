package com.zucc.pbcls.security;

import com.zucc.pbcls.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    com.zucc.pbcls.security.MyUserDetailService myUserDetailService;

    @Autowired
    UserInfoDao userInfoDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/","/toindex","/tologin").permitAll()
                                .antMatchers("/admin/**").hasRole("ADMIN")
                                .antMatchers("/teacher/**").hasAnyRole("ADMIN","TEACHER")
                                .antMatchers("/student/**").hasAnyRole("ADMIN","TEACHER","STUDENT")
                                .and()
                                .formLogin()
                                .loginPage("/tologin")
                                .loginProcessingUrl("/dologin")
                                .failureUrl("/tologin?error=true")
                                .defaultSuccessUrl("/index")
                                .and()
                                .logout().logoutSuccessUrl("/")
                                .and().headers().frameOptions().sameOrigin();

        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        已弃用！该方法为使用自定义LoginAuthenticationProvider类继承DaoAuthenticationProvider,改写抛出异常时的提醒
        auth.authenticationProvider(new LoginAuthenticationProvider(myUserDetailService, userInfoDao));
        auth.userDetailsService(myUserDetailService).passwordEncoder(passwordEncoder());

    }


}
