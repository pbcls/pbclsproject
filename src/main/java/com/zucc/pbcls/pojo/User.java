package com.zucc.pbcls.pojo;


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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private String uid;

    private String name;
    private String pwd;

    @Column(name = "registertime")
    @CreatedDate//Jpa自动设置创建时间
    private Date registerTime;

    private String role;

    @Column(name = "accountnonlocked",columnDefinition="tinyint(1) default 1")
    private boolean accountNonLocked = true;
}
