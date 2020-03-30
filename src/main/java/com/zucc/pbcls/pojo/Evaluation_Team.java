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
@Table(name = "evaluation_team")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Evaluation_Team {
    @Id
    @Column(name = "eteamid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eteamid;

    private int projectid;

    @Column(name = "docpasstime",columnDefinition = "int default 0")
    private int docPassTime;

    @Column(name = "doccorrectness",columnDefinition = "int default 0")
    private int docCorrectness;

    @Column(name = "docinnovation",columnDefinition = "int default 0")
    private int docInnovation;

    @Column(name = "docstyle",columnDefinition = "int default 0")
    private int docStyle;

    @Column(name = "attitude",columnDefinition = "int default 0")
    private int attitude;

    @Column(name = "technique",columnDefinition = "int default 0")
    private int technique;

    @Column(name = "communication",columnDefinition = "int default 0")
    private int communication;

    @Column(name = "cooperation",columnDefinition = "int default 0")
    private int cooperation;

    @Column(name = "score",columnDefinition = "double default 0")
    private double score;

    @Column(name = "mark")
    private String mark;

    @Column(name = "evaluated",columnDefinition = "tinyint default 0")
    private boolean evaluated;


}
