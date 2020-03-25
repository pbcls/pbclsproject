package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zucc.pbcls.pojo.MyUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "project_roletouser")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project_RoleToUser {

    @Id
    @Column(name = "pruid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pruid;

    private int projectid;
    private int roleid;

    @Transient
    private String rolename;

    private String uid;

    @Transient
    private MyUser user;
}
