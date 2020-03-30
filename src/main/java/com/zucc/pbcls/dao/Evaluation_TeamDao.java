package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.Evaluation_Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Evaluation_TeamDao extends JpaRepository<Evaluation_Team,Integer> {
    Evaluation_Team findAllByProjectid(int projectid);
}

