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
@Table(name = "case_role")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class Case_Role {

    @Id
    @Column(name = "roleid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleid;

    @JoinColumn(name = "caseid",foreignKey = @ForeignKey(name = "case_role_case_caseid_fk"))
    private int caseid;

    private String role;
    private String rolename;
}
