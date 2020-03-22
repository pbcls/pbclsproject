package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    private int earlystart;

    private int earlyfinish;

    private int latestart;

    private int latefinish;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date starttime;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date finishtime;

    @Column(name = "needsubmit",columnDefinition = "tinyint default 0")
    private boolean needsubmit;

    @Column(name = "needcheck",columnDefinition = "tinyint default 0")
    private boolean needcheck;

    @Column(name = "status",columnDefinition = "int default 0")
    private int status;

    @Column(name = "canstart",columnDefinition = "tinyint default 0")
    private boolean canstart;

    @Column(name = "canfinish",columnDefinition = "tinyint default 0")
    private boolean canfinish;



    //AOE的操作需要的信息
    @Transient
    private int indegree; // 入度
    @Transient
    private Project_TaskToTask firstarc; //邻接表的第一条边
    @Transient
    private List<Project_TaskToTask> vertices;//邻接表

}
