package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.Evaluation_Mutual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Evaluation_MutualDao extends JpaRepository<Evaluation_Mutual,Integer> {
    Evaluation_Mutual findAllByProjectidAndUidAndTouid(int projectid,String uid,String touid);
    List<Evaluation_Mutual> findAllByProjectidAndUidAndEvaluated(int projectid,String uid,boolean evaluated);
    //实现学生互评的查询 not为教师
    List<Evaluation_Mutual> findAllByProjectidAndUidNotAndTouidAndEvaluated(int projectid,String uid,String touid,boolean evaluated);
    //实现教师评价的查询 uid为教师
    List<Evaluation_Mutual> findAllByProjectidAndUidAndTouidAndEvaluated(int projectid,String uid,String touid,boolean evaluated);
    List<Evaluation_Mutual> findAllByProjectid(int projectid);
}
