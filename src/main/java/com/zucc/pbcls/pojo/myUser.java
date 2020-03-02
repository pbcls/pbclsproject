package com.zucc.pbcls.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class MyUser {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "uid")
    private String uid;
    private String name;
    private String pwd;
    private int sex;

    @Column(name = "registertime")
    @CreatedDate//Jpa自动设置创建时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")//出参时往前端传回一段格式化好的时间字符串
    private Date registerTime;

    private String hobby;
    private String signature;
    private String qq;
    private String wechat;
    private String email;
    private String role;

    @Column(name = "accountnonlocked",columnDefinition="tinyint(1) default 1")
    private boolean accountNonLocked = true;
    private String portrait;

    @Column(name = "loginnum",columnDefinition="int(11) default 0")
    private int loginNum;
}
