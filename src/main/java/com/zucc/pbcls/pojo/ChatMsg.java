package com.zucc.pbcls.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "chatmsg")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class ChatMsg {
    private int projectid;

    @Id
    @Column(name = "chatmsgid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatmsgid;

    private String uid;

    @Column(name = "sendtime")
    @CreatedDate//Jpa自动设置创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//出参时往前端传回一段格式化好的时间字符串
    private Date sendtime;

    private String chatmsg;


    @Transient
    private String portrait;
    @Transient
    private String username;
}

