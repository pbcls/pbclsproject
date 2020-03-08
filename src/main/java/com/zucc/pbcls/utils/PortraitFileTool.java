package com.zucc.pbcls.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class PortraitFileTool {

    public static void uploadFiles(byte[] file, String filePath, String fileName,String oldfileName) throws Exception {
        File targetFile = new File(filePath);
        File oldFile = new File("src/main/resources/static"+oldfileName);

        if (!oldfileName.equals("/img/portrait/default.jpg"))
            oldFile.delete();

//        if (!targetFile.exists()) {
//            targetFile.mkdirs();
//        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
