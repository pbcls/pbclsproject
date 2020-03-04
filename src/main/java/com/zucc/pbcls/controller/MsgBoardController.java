package com.zucc.pbcls.controller;
import com.zucc.pbcls.pojo.MsgBoard;
import com.zucc.pbcls.service.MsgBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("/student")
public class MsgBoardController {

    @Autowired
    MsgBoardService msgBoardService;

    @RequestMapping("/showGetMsg")
    @ResponseBody
    public List<MsgBoard> showGetMsg(@RequestParam(value = "uid") String uid){
        return msgBoardService.showGetMsg(uid);
    }

    @RequestMapping("/showSentMsg")
    @ResponseBody
    public List<MsgBoard> showSentMsg(@RequestParam(value = "uid") String uid,@RequestParam(value = "touid") String touid){
        return msgBoardService.showSentMsg(uid,touid);
    }

    @RequestMapping("/sendMsg")
    @ResponseBody
    public MsgBoard sendMsg(@RequestParam(value = "uid") String uid,@RequestParam(value = "touid") String touid,@RequestParam(value = "msg") String msg){
        return msgBoardService.sendMsg(uid,touid,msg);
    }

}
