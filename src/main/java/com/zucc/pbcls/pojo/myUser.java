package com.zucc.pbcls.pojo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@EntityListeners(AuditingEntityListener.class)//Jpa自动设置创建时间
@Table(name = "user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class MyUser extends BaseRowModel {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "uid")
    @ExcelProperty(value = "账号",index = 0)
    private String uid;

    @ExcelProperty(value = "姓名",index = 1)
    private String name;

    @ExcelProperty(value = "密码(加密)",index = 2)
    private String pwd;
    /**
     * 用户性别，1：男，2：女 0：不详
     */

    @ExcelProperty(value = "性别",index = 3)
    private int sex;

    @Column(name = "registertime")
    @CreatedDate//Jpa自动设置创建时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")//出参时往前端传回一段格式化好的时间字符串
    @ExcelProperty(value = "注册时间",index = 4,format = "yyyy-MM-dd")
    private Date registerTime;

    @ExcelProperty(value = "兴趣",index = 5)
    private String hobby;

    @ExcelProperty(value = "个人签名",index = 6)
    private String signature;

    @ExcelProperty(value = "QQ",index = 7)
    private String qq;

    @ExcelProperty(value = "微信",index = 8)
    private String wechat;

    @ExcelProperty(value = "邮箱",index = 9)
    private String email;

    @ExcelProperty(value = "权限",index = 10)
    private String role;

    @Column(name = "accountnonlocked",columnDefinition="tinyint(1) default 1")
    @ExcelProperty(value = "冻结",index = 11)
    private boolean accountNonLocked = true;

    private String portrait;

    @Column(name = "loginnum",columnDefinition="int(11) default 0")
    @ExcelProperty(value = "登录次数",index = 12)
    private int loginNum;
}
