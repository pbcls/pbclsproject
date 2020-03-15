package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "project")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project {
    @Column(name="caseid")
    private int caseid;

    @Transient
    private String casename;

    @Id
    @Column(name = "projectid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectid;

    private String projectname;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date starttime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date finishtime;

    @Column(name = "status",columnDefinition = "int default 1")
    private int status;

    private int maxplayer;

    private String foldername;


}
