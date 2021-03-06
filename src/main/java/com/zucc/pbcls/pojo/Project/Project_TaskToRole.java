package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zucc.pbcls.pojo.Case.Case_Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "project_tasktorole")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project_TaskToRole {
    @Id
    @Column(name = "ptrid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ptrid;

    private int projectid;
    private int taskid;

    private int roleid;
    @Transient
    private String rolename;

}
