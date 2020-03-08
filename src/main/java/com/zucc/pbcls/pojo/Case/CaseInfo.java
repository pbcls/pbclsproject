package com.zucc.pbcls.pojo.Case;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "caseinfo")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class CaseInfo {

    @Id
    @Column(name = "caseid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int caseid;

    private String casename;
    private String description;

    @CreatedDate//Jpa自动设置创建时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")//出参时往前端传回一段格式化好的时间字符串
    private Date uploadtime;

    private boolean status;
    private int instances;
    private int startedinstances;
    private int finishedinstances;
    private int maxplayer;
    private String foldername;

}
