package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
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
@Table(name = "project_task")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project_Task {

    @EmbeddedId
    private Project_Task_pk projectTaskpk;

    private String taskname;
    private String description;
    private boolean iscritical;
    private int duration;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date earlystart;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date earlyfinish;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date latestart;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date latefinish;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date starttime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date finishtime;

    @Column(name = "needsubmit",columnDefinition = "tinyint default 0")
    private boolean needsubmit;

    @Column(name = "needcheck",columnDefinition = "tinyint default 0")
    private boolean needcheck;

    @Column(name = "status",columnDefinition = "int default 0")
    private boolean status;

    @Column(name = "canstart",columnDefinition = "tinyint default 0")
    private boolean canstart;

    @Column(name = "canfinish",columnDefinition = "tinyint default 0")
    private boolean canfinish;

}
