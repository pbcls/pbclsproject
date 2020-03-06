package com.zucc.pbcls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication()
@EnableJpaAuditing//Jpa自动设置创建时间
public class PbclsApplication {
	public static void main(String[] args) {
		SpringApplication.run(PbclsApplication.class, args);
	}
}
