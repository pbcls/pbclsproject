package com.zucc.pbcls.service;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zucc.pbcls.Listener.ExcelListener;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.ExcelUser;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class Admin_SystemService {
    @Autowired
    UserInfoDao userInfoDao;


    public List<ExcelUser> updateExcel(MultipartFile excel) throws IOException {
        BufferedInputStream buffer = null;
        buffer = new BufferedInputStream(excel.getInputStream());
        ExcelListener excelListener = new ExcelListener();
        ExcelReader excelReader = new ExcelReader(buffer, ExcelTypeEnum.XLSX, null, excelListener);
        excelReader.read(new Sheet(1, 1, ExcelUser.class));
        List<ExcelUser> failresult = excelListener.getDatas();
//        System.out.println("-----------------------------------------------------------");
//        System.out.println(failresult.size());
//        for (ExcelUser e:failresult)
//            System.out.println(e);
        return failresult;
    }

    public List<ExcelUser> findAllExcelUser(){
        List<MyUser> list = userInfoDao.findAll();
        List<ExcelUser> excelUserList = new ArrayList<>();
        for (MyUser user:list){
            ExcelUser excelUser = new ExcelUser();
            excelUser.setUid(user.getUid());
            excelUser.setName(user.getName());
            excelUser.setRegisterTime(user.getRegisterTime());
            excelUser.setHobby(user.getHobby());
            excelUser.setSignature(user.getSignature());
            excelUser.setQq(user.getQq());
            excelUser.setWechat(user.getWechat());
            excelUser.setEmail(user.getEmail());
            excelUser.setLoginNum(user.getLoginNum());
            //显示性别
            if(user.getSex()==0)
                excelUser.setSex("不详");
            else if (user.getSex()==1)
                excelUser.setSex("男");
            else if (user.getSex()==2)
                excelUser.setSex("女");
            //显示权限
            if(user.getRole().equals("ADMIN"))
                excelUser.setRole("管理员");
            else if (user.getRole().equals("TEACHER"))
                excelUser.setRole("教师");
            else if (user.getRole().equals("STUDENT"))
                excelUser.setRole("学生");
            //显示锁定
            if (user.isAccountNonLocked())
                excelUser.setAccountNonLocked("未冻结");
            else
                excelUser.setAccountNonLocked("已冻结");

            excelUserList.add(excelUser);
        }
        return excelUserList;
    }
}
