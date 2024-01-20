package com.artframework.domain.generator;

import com.artframework.domain.constants.BaseEntityConstants;
import com.artframework.domain.utils.FreeMakerTplUtil;
import com.artframework.domain.utils.StreamUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractGenerator {

    protected String basePackage;
    @Setter
    protected String templateFilePath;

    private Map<String,Object> params=new HashMap();

    public void putParam(String key, Object value) {
        this.params.put(key, value);
    }
    public void putParam(Map<String,String> params) {
        this.params.putAll(params);
    }

    public  <T> String generate(T source){
        try (InputStream inputStream = AbstractGenerator.class.getClassLoader().getResourceAsStream(templateFilePath)) {
            String template = StreamUtils.readAsString(inputStream);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("basePackage",this.basePackage);
            paramMap.put("corePackage", BaseEntityConstants.DOMAIN_CORE_PACKAGE);
            paramMap.put("baseMapperClass",BaseEntityConstants.BASE_MAPPER);
            paramMap.putAll(params);
            paramMap.put("source", buildParam(source));
            return FreeMakerTplUtil.process(template, paramMap);
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("generate failed", e);
        }
        return "";
    }

    public abstract <T> Object buildParam(T source);
}
