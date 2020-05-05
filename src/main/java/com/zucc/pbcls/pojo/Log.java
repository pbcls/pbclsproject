package com.zucc.pbcls.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "log")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Log {
    @Id
    @Column(name = "logid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logid;

    @CreatedDate//Jpa自动设置创建时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date logtime;

    private int caseid;
    @Transient
    private String casename;

    private int projectid;
    @Transient
    private String projectname;

    private int taskid;
    @Transient
    private String taskname;

    private int roleid;
    @Transient
    private String rolename;

    private String uid;
    @Transient
    private String username;

    private String touid;
    @Transient
    private String tousername;

    private int type;
    private boolean needpass;

    @Column(name = "passstatus",columnDefinition = "int default 0")
    private int passstatus;

    private String logmsg;
}
