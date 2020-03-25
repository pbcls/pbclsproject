package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "project_role")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project_Role {

    @Id
    @Column(name = "prid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prid;

    private int projectid;

    private int roleid;

    private String rolename;
}
