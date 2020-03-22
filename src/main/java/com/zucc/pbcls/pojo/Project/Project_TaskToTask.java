package com.zucc.pbcls.pojo.Project;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "project_tasktotask")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project_TaskToTask {
    @Id
    @Column(name = "pttid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pttid;

    private int projectid;

    private int predecessorid;


    private int successorid;

    private String type;

    @Transient
    private String taskname;


    //AOE的操作需要的信息
    @Transient
    Project_Task adjvex;
    @Transient
    Project_TaskToTask nextarc;

}
