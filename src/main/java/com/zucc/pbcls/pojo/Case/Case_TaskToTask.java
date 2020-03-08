package com.zucc.pbcls.pojo.Case;


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
@Table(name = "case_tasktotask")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Case_TaskToTask {

    @Id
    @Column(name = "cttid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cttid;

    private int caseid;

    private int predecessorid;


    private int successorid;

    private String type;

    @Transient
    private String taskname;
}
