package io.github.roger3lee.domain.v2.utils;

import lombok.extern.slf4j.Slf4j;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class FreeMakerTplUtil {
    private static Configuration cfg;

    static {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_30);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_30));
            cfg.setTemplateLoader(new StringTemplateLoader());
            cfg.setSharedVariable("NameUtils", new NameUtils());
            cfg.setClassicCompatibleAsInt(1);
        } catch (TemplateModelException e) {
            log.error("Failed to initialize FreeMarker configuration", e);
        }
    }

    public static String process(String template, Map<String, Object> paramMap) {
        try (StringWriter writer = new StringWriter()) {
            final Template templateEngine = new Template("default", template, cfg);
            templateEngine.process(paramMap, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            log.error("FreeMarker template processing failed", e);
        }
        return null;
    }
}
