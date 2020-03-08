package com.zucc.pbcls.pojo.Case;


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
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "case_task")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Case_Task {

    @EmbeddedId
    private Case_Task_pk caseTaskpk;

    private String taskname;
    private String description;
    private int dutarion;
}
