package com.zucc.pbcls.pojo;


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
@Table(name = "evaluation_mutual")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Evaluation_Mutual {
    @Id
    @Column(name = "emutualid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emutualid;

    private int projectid;
    private String uid;
    private String touid;

    @Column(name = "attitude",columnDefinition = "int default 0")
    private int attitude;

    @Column(name = "technique",columnDefinition = "int default 0")
    private int technique;

    @Column(name = "communication",columnDefinition = "int default 0")
    private int communication;

    @Column(name = "cooperation",columnDefinition = "int default 0")
    private int cooperation;

    @Column(name = "organization",columnDefinition = "int default 0")
    private int organization;

    @Column(name = "decision",columnDefinition = "int default 0")
    private int decision;

    @Column(name = "helpme",columnDefinition = "int default 0")
    private int helpme;

    @Column(name = "score",columnDefinition = "float default 0")
    private float score;

    @Column(name = "mark")
    private String mark;

    @Column(name = "isevaluated",columnDefinition = "tinyint default 0")
    private boolean isEvaluated;



}
