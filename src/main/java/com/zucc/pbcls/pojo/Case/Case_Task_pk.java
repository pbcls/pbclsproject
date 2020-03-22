package com.zucc.pbcls.pojo.Case;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Component
public class Case_Task_pk implements Serializable {
    @Column(name="caseid")
    private int caseid;

    @Column(name = "taskid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskid;

}
