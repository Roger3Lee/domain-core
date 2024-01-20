package com.artframework.domain.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static void saveFile(String path, String fileName, String text) throws IOException {
        saveFile(path, fileName, text, true);
    }
    public static void saveFile(String path, String fileName, String text, boolean overWrite) throws IOException {
        File file = new File(path);
        file.mkdirs();
        file = new File(path, fileName);
        if (!file.exists()) {
            file.createNewFile();
        }else{
            if(!overWrite){
                return;
            }
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(text.getBytes());
        }
    }
}
