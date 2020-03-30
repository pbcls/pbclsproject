package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.Evaluation_Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Evaluation_MemberDao extends JpaRepository<Evaluation_Member,Integer> {
    Evaluation_Member findAllByProjectidAndTaskidAndUid(int projectid,int taskid,String uid);
    List<Evaluation_Member> findAllByProjectidAndTaskid(int projectid,int taskid);
    List<Evaluation_Member> findAllByProjectidAndUidAndPmEvaluated(int projectid,String uid,boolean isPmEvaluated);
    List<Evaluation_Member> findAllByProjectidAndUidAndSelfEvaluated(int projectid,String uid,boolean isSelfEvaluated);
    List<Evaluation_Member> findAllByProjectid(int projectid);
}
