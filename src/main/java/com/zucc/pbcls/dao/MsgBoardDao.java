package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.MsgBoard;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MsgBoardDao extends JpaRepository<MsgBoard,Integer> {
    List<MsgBoard> findAllByUser(MyUser user);
    List<MsgBoard> findAllByToUser(MyUser user);
    List<MsgBoard> findAllByUserAndToUser(MyUser user,MyUser touser);
    List<MsgBoard> findAllByToUser(Pageable pageable, MyUser user);
}
