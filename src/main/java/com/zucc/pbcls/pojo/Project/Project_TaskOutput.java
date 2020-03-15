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
@Table(name = "project_taskoutput")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Project_TaskOutput {

    @Id
    @Column(name = "ptoid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ptoid;

    private int projectid;
    private int taskid;
    private String taskoutput;

}
