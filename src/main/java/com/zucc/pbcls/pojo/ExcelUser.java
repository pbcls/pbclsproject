package com.zucc.pbcls.pojo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelUser extends BaseRowModel {

    @ExcelProperty(value = "账号",index = 0)
    private String uid;

    @ExcelProperty(value = "姓名",index = 1)
    private String name;

    /**
     * 用户性别，1：男，2：女 0：不详
     */
    @ExcelProperty(value = "性别(男/女/不详)",index = 2)
    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")//出参时往前端传回一段格式化好的时间字符串
    @ExcelProperty(value = "注册时间(导入数据自动取当前时间为注册时间)",index = 3,format = "yyyy-MM-dd")
    private Date registerTime;

    @ExcelProperty(value = "兴趣",index = 4)
    private String hobby;

    @ExcelProperty(value = "个人签名",index = 5)
    private String signature;

    @ExcelProperty(value = "QQ",index = 6)
    private String qq;

    @ExcelProperty(value = "微信",index = 7)
    private String wechat;

    @ExcelProperty(value = "邮箱",index = 8)
    private String email;

    @ExcelProperty(value = "权限(管理员/教师/学生)",index = 9)
    private String role;

    @ExcelProperty(value = "冻结(未冻结/已冻结)",index = 10)
    private String accountNonLocked;

    @ExcelProperty(value = "登录次数(导入无需填写,默认为0)",index = 11)
    private int loginNum;
}
