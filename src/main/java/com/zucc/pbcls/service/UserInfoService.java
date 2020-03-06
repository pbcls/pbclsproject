package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.utils.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserInfoService {
    @Autowired
    UserInfoDao userInfoDao;

    public MyUser showUserDetail(String uid){
        return userInfoDao.findByUid(uid);
    }

    public boolean updateUserDetial(MyUser user){
        if (userInfoDao.findByUid(user.getUid())!=null) {
            userInfoDao.save(user);
            return true;
        }
        else
            return false;
    }

    public boolean changePwd(String uid ,String pwd,String newpwd){
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        MyUser user = userInfoDao.findByUid(uid);
        if (bc.matches(pwd,user.getPwd())){
            user.setPwd(bc.encode(newpwd));
            userInfoDao.save(user);
            return true;
        }else
            return false;
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
        String oldfileName = userInfoDao.findByUid(uid).getPortrait();
        String fileName = file.getOriginalFilename();
        fileName = FileTool.renameToUUID(fileName);
        System.out.println(fileName);
        try {
            FileTool.uploadFiles(file.getBytes(),filePath, fileName,oldfileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "/img/portrait/"+fileName;
        myUser.setPortrait(url);
        userInfoDao.save(myUser);
        return url;
    }
}
