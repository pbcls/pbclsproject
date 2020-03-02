package com.zucc.pbcls.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    myUserService myUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/","/tologin").permitAll()
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
                                .logout().logoutSuccessUrl("/");

        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        已弃用！该方法为使用自定义LoginAuthenticationProvider类继承DaoAuthenticationProvider,改写抛出异常时的提醒
//        auth.authenticationProvider(new LoginAuthenticationProvider(myUserService));
        auth.userDetailsService(myUserService).passwordEncoder(passwordEncoder());

    }


}
