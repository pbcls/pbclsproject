package com.zucc.pbcls.utils;

import com.zucc.pbcls.dao.Case.CaseDao;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.Project.Project;
import com.zucc.pbcls.pojo.Project.Project_Task;
import com.zucc.pbcls.pojo.Project.Project_TaskOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectFileUtil {
    public void InitDirectory(String filePath) throws Exception {
        File targetFile = new File("src/main/resources/static/case" + filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
    }

    public void copyCDocToPDoc(String oldfilepath, String newfilepath) {
        try {
            oldfilepath = "src/main/resources/static/case" + oldfilepath;
            File old = new File(oldfilepath);
            String[] file = old.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldfilepath.endsWith(File.separator)) {
                    temp = new File(oldfilepath + file[i]);
                } else {
                    temp = new File(oldfilepath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream("src/main/resources/static/case" + newfilepath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }

    public List<String> getProjectFileList(Project project) {
        String path = "src/main/resources/static/case" + project.getFoldername() + "/DOCS";
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

    public String DownloadProjectFile(String filename, Project project, HttpServletResponse response) {
        String downloadFilePath = "src/main/resources/static/case" + project.getFoldername() + "/DOCS/" + filename;//被下载的文件在服务器中的路径,
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

    public void uploadDOCSFiles(byte[] file, String filePath, String fileName) throws Exception {
//        File targetFile = new File(filePath);
        FileOutputStream out = new FileOutputStream("src/main/resources/static/case" + filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public void submitTaskFiles(byte[] submitfile, String filePath, String fileName) throws Exception {
        filePath = "src/main/resources/static/case" + filePath;
        File task = new File(filePath);
        File[] files = task.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                delProjectFile(file);//递归删除
            } else {
                file.delete();
                System.out.println("删除" + file.getAbsolutePath());
            }
        }

        FileOutputStream out = new FileOutputStream(filePath + File.separator + fileName);
        out.write(submitfile);
        out.flush();
        out.close();
    }

    public void delProjectFile(File project) {
        if (!project.isDirectory()) {
            project.delete();
        } else {
            File[] files = project.listFiles();

            // 空文件夹
            if (files.length == 0) {
                project.delete();
                System.out.println("删除" + project.getAbsolutePath());
                return;
            }

            // 删除子文件夹和子文件
            for (File file : files) {
                if (file.isDirectory()) {
                    delProjectFile(file);//递归删除
                } else {
                    file.delete();
                    System.out.println("删除" + file.getAbsolutePath());
                }
            }

            // 删除文件夹本身
            project.delete();
            System.out.println("删除" + project.getAbsolutePath());
        }
    }

    public List<String> getTaskFileList(Project_TaskOutput taskOutput) {
        String path = "src/main/resources/static/case" + taskOutput.getTaskoutput();
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

    public String DownloadProjectTaskFile(String filename, Project_TaskOutput project_taskOutput, HttpServletResponse response) {
        String downloadFilePath = "src/main/resources/static/case" + project_taskOutput.getTaskoutput() + "/" + filename;//被下载的文件在服务器中的路径,
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
}
