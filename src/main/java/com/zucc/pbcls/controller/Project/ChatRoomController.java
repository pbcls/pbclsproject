package com.zucc.pbcls.controller.Project;

import com.zucc.pbcls.dao.ChatMsgDao;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.ChatMsg;
import com.zucc.pbcls.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;

@Controller
public class ChatRoomController {
    @Autowired
    ChatMsgDao chatMsgDao;
    @Autowired
    UserInfoDao userInfoDao;

    @RequestMapping("/chatroom/sendmsg")
    @ResponseBody
    public ChatMsg sendMsg(@RequestParam(value = "projectid") int projectid, @RequestParam(value = "msg") String msg){
        UserInfo user = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setChatmsg(msg);
        chatMsg.setUid(user.getUsername());
        chatMsg.setProjectid(projectid);
        chatMsg = chatMsgDao.save(chatMsg);
        return chatMsg;
    }

    @RequestMapping("/chatroom/showmsg")
    @ResponseBody
    public List<ChatMsg> showMsg(@RequestParam(value = "projectid") int projectid){
        List<ChatMsg> chatMsgs = chatMsgDao.findtop10(projectid);
        return chatMsgs;
    }


}
