package com.zucc.pbcls.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "evaluation_member")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Evaluation_Member {
    @Id
    @Column(name = "ememberid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ememberid;

    private int projectid;
    private int taskid;
    private int roleid;
    private String uid;

    @Column(name = "selfattitude",columnDefinition = "int default 0")
    private int selfAttitude;
    @Column(name = "selftechnique",columnDefinition = "int default 0")
    private int selfTechnique;
    @Column(name = "selfcommunication",columnDefinition = "int default 0")
    private int selfCommunication;
    @Column(name = "selfcooperation",columnDefinition = "int default 0")
    private int selfCooperation;
    @Column(name = "selfachievement",columnDefinition = "int default 0")
    private int selfAchievement;
    @Column(name = "selforganization",columnDefinition = "int default 0")
    private int selfOrganization;
    @Column(name = "selfdecision",columnDefinition = "int default 0")
    private int selfDecision;
    @Column(name = "selfscore",columnDefinition = "double default 0")
    private double selfScore;
    @Column(name = "selfmark")
    private String selfMark;
    @Column(name = "selfexpectation")
    private String selfExpectation;
    @Column(name = "selfevaluated",columnDefinition = "tinyint default 0")
    private boolean selfEvaluated;
    @Column(name = "pmattitude",columnDefinition = "int default 0")
    private int pmAttitude;
    @Column(name = "pmtechnique",columnDefinition = "int default 0")
    private int pmTechnique;
    @Column(name = "pmcommunication",columnDefinition = "int default 0")
    private int pmCommunication;
    @Column(name = "pmcooperation",columnDefinition = "int default 0")
    private int pmCooperation;
    @Column(name = "pmdocpasstime",columnDefinition = "int default 0")
    private int pmDocPassTime;
    @Column(name = "pmdocpassrate",columnDefinition = "int default 0")
    private int pmDocPassRate;
    @Column(name = "pmdoccorrectness",columnDefinition = "int default 0")
    private int pmDocCorrectness;
    @Column(name = "pmdocinnovation",columnDefinition = "int default 0")
    private int pmDocInnovation;
    @Column(name = "pmdocstyle",columnDefinition = "int default 0")
    private int pmDocStyle;
    @Column(name = "pmscore",columnDefinition = "double default 0")
    private double pmScore;
    @Column(name = "pmmark")
    private String pmMark;
    @Column(name = "pmevaluated",columnDefinition = "tinyint default 0")
    private boolean pmEvaluated;

}
