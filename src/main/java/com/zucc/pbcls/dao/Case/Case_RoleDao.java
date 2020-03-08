package com.zucc.pbcls.dao.Case;

import com.zucc.pbcls.pojo.Case.Case_Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Case_RoleDao extends JpaRepository<Case_Role,Integer> {
    List<Case_Role> findAllByCaseid(int caseid);
    List<Case_Role>findAllByRoleid(int roleid);
    Case_Role findAllByCaseidAndRoleid(int caseid,int roleid);
}
