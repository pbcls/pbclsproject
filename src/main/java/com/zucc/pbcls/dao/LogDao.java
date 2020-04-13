package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogDao extends JpaRepository<Log,Integer> {

    @Query(nativeQuery = true,value = "select * from Log where touid like ?1")
    List<Log> findAllByTouid(String touid, Pageable pageable);


    List<Log> findAllByProjectid(int projectid);
    List<Log> findAllByCaseid(int caseid);
    Log findByLogid(int logid);
}
