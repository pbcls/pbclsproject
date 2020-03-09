package com.zucc.pbcls.dao.Case;

import com.zucc.pbcls.pojo.Case.CaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CaseDao extends JpaRepository<CaseInfo,Integer> ,JpaSpecificationExecutor<CaseInfo> {
    List<CaseInfo> findAll();
    CaseInfo findAllByCaseid(int caseid);
}
