package com.artframework.domain.utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author Zou Yulun
 */
@Slf4j
public class FreeMakerTplUtil {


    private static Configuration getCfg() throws TemplateModelException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_30));
        cfg.setTemplateLoader(new StringTemplateLoader());
        cfg.setSharedVariable("NameUtils", new NameUtils());
        cfg.setClassicCompatibleAsInt(1);
        return cfg;
    }

    /**
     * xml模板轉化處理
     *
     * @param template 模板文本
     * @param paramMap 替換參數
     */
    public static String process(String template, Map<String, Object> paramMap) throws TemplateModelException {
        Configuration configuration = getCfg();
        try (StringWriter writer = new StringWriter()) {
            final Template templateEngine = new Template("default", template, configuration);
            templateEngine.process(paramMap, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            log.error("", e);
        }
        return null;
    }
}
