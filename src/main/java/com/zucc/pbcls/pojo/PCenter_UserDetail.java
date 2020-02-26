package com.zucc.pbcls.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "userdetail")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Component
@ToString
public class PCenter_UserDetail {

    @Id
    @Column(name = "uid")
    private String uid;

    private int sex;
    private String hobby;
    private String signature;
    private String qq;
    private String wechat;
    private String email;
    private String portrait;
}
