package com.zucc.pbcls.controller;


import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.exception.ExcelGenerateException;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zucc.pbcls.Listener.ExcelListener;
import com.zucc.pbcls.pojo.ExcelUser;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.service.Admin_SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.apache.tomcat.util.file.ConfigFileLoader.getInputStream;

@Controller
public class Admin_SystemController {

    @Autowired
    private Admin_SystemService admin_systemService;

    @RequestMapping("/updateExcel")
    @ResponseBody
    public List<ExcelUser> updateExcel(@RequestParam(value = "excel")MultipartFile excel) throws IOException {
            return admin_systemService.updateExcel(excel);
    }

    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<ExcelUser> excelUserList = admin_systemService.findAllExcelUser();
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        String fileName = "用户信息表";
        Sheet sheet = new Sheet(1, 0,ExcelUser.class);
        //设置自适应宽度
        sheet.setAutoWidth(Boolean.TRUE);
        // 第一个 sheet 名称
        sheet.setSheetName("用户信息");
        writer.write(excelUserList, sheet);
        //通知浏览器以附件的形式下载处理，设置返回头要注意文件名有中文
        response.setHeader("Content-disposition", "attachment;filename=" + new String( fileName.getBytes("utf8"), "ISO8859-1" ) + ".xlsx");
        writer.finish();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        out.flush();
    }

    @RequestMapping("/exportModelExcel")
    public void exportModelExcel(HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        String fileName = "用户信息表";
        Sheet sheet = new Sheet(1, 0,ExcelUser.class);
        //设置自适应宽度
        sheet.setAutoWidth(Boolean.TRUE);
        // 第一个 sheet 名称
        sheet.setSheetName("用户信息");
        writer.write(null, sheet);
        //通知浏览器以附件的形式下载处理，设置返回头要注意文件名有中文
        response.setHeader("Content-disposition", "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" ) + ".xlsx");
        writer.finish();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        out.flush();
    }

}
