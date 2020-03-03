package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.MsgBoardDao;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MsgBoard;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgBoardService {

    @Autowired
    MsgBoardDao msgBoardDao;
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    MsgBoard msgBoard;

    //在自己个人空间查看别人给我的留言,给我所以是ToUser
    //GetMsg我得到的留言
    public List<MsgBoard> showGetMsg(String uid){
        MyUser user = userInfoDao.findByUid(uid);
        return msgBoardDao.findAllByToUser(user);
    }

    //在别人空间留言前能看到我之前给他的留言;uid为我,touid为别人
    //SentMsg我发出去的留言
    public List<MsgBoard> showSentMsg(String uid,String touid){
        MyUser user = userInfoDao.findByUid(uid);
        MyUser touser = userInfoDao.findByUid(touid);
        return msgBoardDao.findAllByUserAndToUser(user,touser);
    }

    //给别人留言,这里的uid是我,touid是别人
    public MsgBoard sendMsg(String uid,String touid,String msg){
        MyUser user = userInfoDao.findByUid(uid);
        MyUser touser = userInfoDao.findByUid(touid);
        msgBoard.setUser(user);
        msgBoard.setToUser(touser);
        msgBoard.setMsg(msg);
        return msgBoardDao.save(msgBoard);
    }



}
