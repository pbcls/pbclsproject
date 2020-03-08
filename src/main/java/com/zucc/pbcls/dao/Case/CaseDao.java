package com.zucc.pbcls.dao.Case;

import com.zucc.pbcls.pojo.Case.CaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseDao extends JpaRepository<CaseInfo,Integer> {
    List<CaseInfo> findAll();
    CaseInfo findAllByCaseid(int caseid);
}
