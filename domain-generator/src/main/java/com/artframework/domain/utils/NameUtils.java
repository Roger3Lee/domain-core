package com.artframework.domain.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

public class NameUtils {

    public static String getNameWithSuffix(String name, String suffix) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", name))) + suffix;
    }

    public static String dataObjectName(String name) {
        return getNameWithSuffix(name, "DO");
    }

    public static String dataTOName(String name) {
        return getNameWithSuffix(name, "DTO");
    }
    public static String mapperName(String name) {
        return getNameWithSuffix(name, "Mapper");
    }

    public String genGetter(String fieldName){
        return StrUtil.genGetter(StrUtil.toCamelCase(StrUtil.format("{}", fieldName)));
    }

}
