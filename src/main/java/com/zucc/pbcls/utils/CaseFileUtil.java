package com.zucc.pbcls.utils;

import com.zucc.pbcls.pojo.Case.CaseInfo;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CaseFileUtil {


    public List<String> getCaseFileList(CaseInfo caseInfo) {
        String path = "src/main/resources/static/case" + caseInfo.getFoldername() + "/DOCS";
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
//                files.add(tempList[i].toString());
                //文件名，不包含路径
                files.add(tempList[i].getName());
            }
        }
        for (String f : files) {
            System.out.println(f);
        }
        return files;
    }


    public String DownloadCaseFile(String filename,CaseInfo caseInfo,HttpServletResponse response) {
        String downloadFilePath = "src/main/resources/static/case"+caseInfo.getFoldername()+"/DOCS/"+filename;//被下载的文件在服务器中的路径,
        String fileName = filename;//被下载文件的名称

        File file = new File(downloadFilePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

                return "下载成功";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "下载失败";
    }


    public void delCaseFile(File casefile) {
        if (!casefile.isDirectory()) {
            casefile.delete();
        } else {
            File[] files = casefile.listFiles();

            // 空文件夹
            if (files.length == 0) {
                casefile.delete();
                System.out.println("删除" + casefile.getAbsolutePath());
                return;
            }

            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    delCaseFile(file);//递归删除
                } else {
                    file.delete();
                    System.out.println("删除" + file.getAbsolutePath());
                }
            }

            // 删除文件夹本身
            casefile.delete();
            System.out.println("删除" + casefile.getAbsolutePath());
        }
    }
}

