package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.utils.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserInfoService {
    @Autowired
    UserInfoDao userInfoDao;

    public MyUser showUserDetail(String uid){
        return userInfoDao.findByUid(uid);
    }

    public void updateUserDetial(MyUser pCenter_userDetail){
        userInfoDao.save(pCenter_userDetail);
    }

    /**
     *0 file is null
     *1 user is not exist
     *2 success
     */
    public String uploadPortrait(MultipartFile file, String uid){
        MyUser myUser = userInfoDao.findByUid(uid);
        if (file == null){
            System.out.println("文件为空!");
            return "0";
        }
        if (myUser == null) {
            System.out.println("用户不存在!");
            return "1";
        }
        //springboot默认在项目当前路径下 即为classpath: 直接调用以下路径即可
        String filePath =  "src/main/resources/static/img/portrait/";
        String fileName = file.getOriginalFilename();
        fileName = FileTool.renameToUUID(fileName);
        System.out.println(fileName);
        try {
            FileTool.uploadFiles(file.getBytes(),filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "/img/portrait/"+fileName;
        myUser.setPortrait(url);
        userInfoDao.save(myUser);
        return url;
    }
}
