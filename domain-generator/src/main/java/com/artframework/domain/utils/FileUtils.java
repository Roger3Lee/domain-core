package com.artframework.domain.utils;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static void saveFile(String path, String fileName, String text) throws IOException {
        saveFile(path, fileName, text, true);
    }
    public static void saveFile(String path, String fileName, String text, boolean overWrite) throws IOException {
        //換行符轉換成LF
        text = text.replace("\r\n", "\n");
        boolean isCreate = false;
        File file = new File(path);
        file.mkdirs();
        file = new File(path, fileName);
        if (!file.exists()) {
            file.createNewFile();
            isCreate = true;
        } else {
            if (!overWrite) {
                return;
            }
        }

        //讀取文件類容對比， 如果相同則不需要寫文件
        if(!isCreate && readFile(file).equals(text)){
            return;
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            osw.write(text);
            osw.flush();
        }
    }

    public static String readFile(File file) {
        return FileUtil.readString(file, StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
