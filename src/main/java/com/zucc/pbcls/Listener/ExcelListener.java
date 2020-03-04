package com.zucc.pbcls.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.ExcelUser;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelListener extends AnalysisEventListener {
    //自定义用于暂时存储data
    //可以通过实例获取该值

    public static UserInfoDao userInfoDao;


    private List<ExcelUser> datas = new ArrayList<ExcelUser>();

    public void invoke(Object object, AnalysisContext context) {
        doSomething(object,context);//获取每条数据,做入库处理
    }
    private void doSomething(Object object,AnalysisContext context) {
        //入库调用接口
        ExcelUser excelUser = (ExcelUser)object;
        if(userInfoDao.findByUid(excelUser.getUid())!=null) {
            datas.add(excelUser);//若出现已存在相同uid，则放入dates中,用于返回错误信息
            return;
        }
        MyUser user = new MyUser();
        user.setUid(excelUser.getUid());
        user.setName(excelUser.getName());
        user.setPwd(new BCryptPasswordEncoder().encode(excelUser.getUid()));
        user.setRegisterTime(excelUser.getRegisterTime());
        user.setHobby(excelUser.getHobby());
        user.setSignature(excelUser.getSignature());
        user.setQq(excelUser.getQq());
        user.setWechat(excelUser.getWechat());
        user.setEmail(excelUser.getEmail());
        user.setPortrait("/img/portrait/default.jpg");
        user.setLoginNum(excelUser.getLoginNum());

        if (excelUser.getSex().equals("不详"))
            user.setSex(0);
        else if (excelUser.getSex().equals("男"))
            user.setSex(1);
        else if (excelUser.getSex().equals("女"))
            user.setSex(2);

        if (excelUser.getRole().equals("管理员"))
            user.setRole("ADMIN");
        else if (excelUser.getRole().equals("教师"))
            user.setRole("TEACHER");
        else if (excelUser.getRole().equals("学生"))
            user.setRole("STUDENT");

        if (excelUser.getAccountNonLocked().equals("未冻结"))
            user.setAccountNonLocked(true);
        else if (excelUser.getAccountNonLocked().equals("已冻结"))
            user.setAccountNonLocked(false);

//        System.out.println("当前行："+context.getCurrentRowNum());
//        System.out.println(user);
        userInfoDao.save(user);

    }
    public void doAfterAllAnalysed(AnalysisContext context) {
        // datas.clear();//解析结束销毁不用的资源
    }
    public List<ExcelUser> getDatas() {
        return datas;
    }
    public void setDatas(List<ExcelUser> datas) {
        this.datas = datas;
    }

    public static UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public static void setUserInfoDao(UserInfoDao userInfoDao) {
        ExcelListener.userInfoDao = userInfoDao;
    }
}
