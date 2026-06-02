package io.github.roger3lee.domain.v2.utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileUtils {
    public static void saveFile(String path, String fileName, String text) throws IOException {
        saveFile(path, fileName, text, true);
    }

    public static void saveFile(String path, String fileName, String text, boolean overWrite) throws IOException {
        text = text.replace("\r\n", "\n");
        File dir = new File(path);
        dir.mkdirs();
        File file = new File(path, fileName);

        if (!file.exists()) {
            file.createNewFile();
        } else {
            if (!overWrite) {
                log.info("File already exists (skip): {}", file.getAbsolutePath());
                return;
            }
        }

        if (file.exists() && readFile(file).equals(text)) {
            log.info("File unchanged (skip): {}", file.getAbsolutePath());
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write(text);
            osw.flush();
        }
        log.info("Generated: {}", file.getAbsolutePath());
    }

    public static String readFile(File file) {
        return FileUtil.readString(file, StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
