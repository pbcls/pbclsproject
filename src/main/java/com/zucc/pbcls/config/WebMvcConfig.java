package com.zucc.pbcls.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
    @Override
    //映射图片保存地址
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        file:/Users/honmono/Study/pbclsproject/src/main/resources/static/   留着！
        registry.addResourceHandler("/**").addResourceLocations("file:D:\\Ij\\pbclsproject\\src\\main\\resources\\static\\");
    }

}