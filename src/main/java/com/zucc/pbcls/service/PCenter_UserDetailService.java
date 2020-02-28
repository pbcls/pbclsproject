package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.PCenter_UserDetailDao;
import com.zucc.pbcls.pojo.PCenter_UserDetail;
import com.zucc.pbcls.utils.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class PCenter_UserDetailService {
    @Autowired
    PCenter_UserDetailDao pCenter_userDetailDao;

    public PCenter_UserDetail showUserDetail(String uid){
        PCenter_UserDetail pcud = pCenter_userDetailDao.findByUid(uid);
        if(pcud.getPortrait() == null)
            pcud.setPortrait("/img/portrait/default.jpg");
        return pcud;
    }

    public void updateUserDetial(PCenter_UserDetail pCenter_userDetail){
        pCenter_userDetailDao.save(pCenter_userDetail);
    }

    /**
     *0 file is null
     *1 user is not exist
     *2 success
     */
    public String uploadPortrait(MultipartFile file, String uid){
        PCenter_UserDetail pcud = pCenter_userDetailDao.findByUid(uid);
        if (file == null){
            System.out.println("文件为空!");
            return "0";
        }
        if (pcud == null) {
            System.out.println("用户不存在!");
            return "1";
        }
        //springboot默认在项目当前路径下 即为classpath: 直接调用以下路径即可
        String filePath =  "src/main/resources/static/img/portrait/";
        String fileName = file.getOriginalFilename();
        fileName = FileTool.renameToUUID(fileName);
        try {
            FileTool.uploadFiles(file.getBytes(),filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "/img/portrait/"+fileName;
        pcud.setPortrait(url);
        pCenter_userDetailDao.save(pcud);
        return url;
    }
}
