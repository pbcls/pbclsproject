package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogDao extends JpaRepository<Log,Integer> {
    @Query("select l from Log l where l.touid like ?1 ORDER BY l.logid DESC")
    List<Log> findAllByTouid(String touid);

    Log findByLogid(int logid);
}
