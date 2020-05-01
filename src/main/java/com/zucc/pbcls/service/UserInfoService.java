package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.Project.ProjectDao;
import com.zucc.pbcls.dao.Project.Project_RoleDao;
import com.zucc.pbcls.dao.Project.Project_RoleToUserDao;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.pojo.Project.Project_Role;
import com.zucc.pbcls.pojo.Project.Project_RoleToUser;
import com.zucc.pbcls.utils.PortraitFileTool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class UserInfoService {
    @Autowired
    UserInfoDao userInfoDao;
    @Autowired
    Project_RoleToUserDao project_roleToUserDao;
    @Autowired
    ProjectDao projectDao;
    @Autowired
    Project_RoleDao project_roleDao;

    public MyUser showUserDetail(String uid){
        return userInfoDao.findByUid(uid);
    }

    public JSONArray findAllProjectByUid(String uid) {
        List<Project_RoleToUser> project_roleToUsers = project_roleToUserDao.findAllByUid(uid);
        JSONArray json_projects = new JSONArray();
        for (int i = 0; i < project_roleToUsers.size(); i++) {
            project_roleToUsers.get(i).setRolename(project_roleDao.findAllByProjectidAndRoleid(project_roleToUsers.get(i).getProjectid()
                    , project_roleToUsers.get(i).getRoleid()).getRolename());
            JSONObject json_project_roleToUser = new JSONObject(project_roleToUsers.get(i));
            JSONObject json_project = new JSONObject(projectDao.findAllByProjectid(project_roleToUsers.get(i).getProjectid()));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("project", json_project);
            jsonObject.put("role", json_project_roleToUser);
            json_projects.put(i, jsonObject);
        }
        return json_projects;
    }

    public boolean updateUserDetial(MyUser user){
        MyUser olduser = userInfoDao.findByUid(user.getUid());
        if (olduser!=null){
            olduser.setName(user.getName());
            olduser.setSex(user.getSex());
            olduser.setHobby(user.getHobby());
            olduser.setSignature(user.getSignature());
            olduser.setQq(user.getQq());
            olduser.setWechat(user.getWechat());
            olduser.setEmail(user.getEmail());
            userInfoDao.save(olduser);
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
        fileName = PortraitFileTool.renameToUUID(fileName);
        System.out.println(fileName);
        try {
            PortraitFileTool.uploadFiles(file.getBytes(),filePath, fileName,oldfileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "/img/portrait/"+fileName;
        myUser.setPortrait(url);
        userInfoDao.save(myUser);
        return url;
    }
}
