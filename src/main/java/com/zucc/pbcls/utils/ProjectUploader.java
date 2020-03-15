package com.zucc.pbcls.utils;

import java.io.File;
import java.io.FileOutputStream;

public class ProjectUploader {

    public void InitDirectory(String filePath) throws Exception {
        File targetFile = new File("src/main/resources/static/case"+filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
    }
}
