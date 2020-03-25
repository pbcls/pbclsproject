package com.zucc.pbcls.pojo.Case;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "case_tasktorole")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Case_TaskToRole {

    @Id
    @Column(name = "ctrid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ctrid;

    private int caseid;
    private int taskid;

    @ManyToOne()
    @JoinColumn(name = "roleid",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    @NotFound(action= NotFoundAction.IGNORE)
    private Case_Role case_role;

}
