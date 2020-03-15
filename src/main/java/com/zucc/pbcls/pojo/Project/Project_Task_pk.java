package com.zucc.pbcls.pojo.Project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Component
public class Project_Task_pk implements Serializable {

    @Column(name="projectid")
    private int projectid;

    @Column(name = "taskid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskid;
}
