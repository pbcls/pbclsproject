package com.zucc.pbcls.dao;

import com.zucc.pbcls.pojo.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMsgDao extends JpaRepository<ChatMsg,Integer> {
    @Query(nativeQuery = true,value = "SELECT * from chatmsg WHERE projectid=(:projectid) ORDER BY chatmsgid desc limit 0,10")
    List<ChatMsg> findtop10 (@Param("projectid") int projectid);
}
