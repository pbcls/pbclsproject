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
@Table(name = "msgboard")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Component
public class MsgBoard {
    @Id
    @Column(name = "msgid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int msgid;

    @ManyToOne()
    @JoinColumn(name = "uid",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private MyUser user;

    @ManyToOne()
    @JoinColumn(name = "touid",foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    private MyUser toUser;

    @Column(name = "msgtime")
    @CreatedDate//Jpa自动设置创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//出参时往前端传回一段格式化好的时间字符串
    private Date msgTime;

    private String msg;
}
