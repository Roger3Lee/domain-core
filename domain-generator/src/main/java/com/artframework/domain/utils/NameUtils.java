package com.artframework.domain.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

public class NameUtils {
    public static String getName(String name) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", name)));
    }

    public static String getNameWithSuffix(String name, String suffix) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", name))) + suffix;
    }

    public static String getNameWithPrefix(String name, String prefix) {
        return prefix + StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", name)));
    }


    public static String getFieldName(String name) {
        return StringUtils.uncapitalize(StrUtil.toCamelCase(name));
    }

    public static String getFieldWithSuffix(String name, String suffix) {
        return StrUtil.toCamelCase(StrUtil.format("{}", name)) + StringUtils.capitalize(suffix);
    }

    public static String getFieldWithPrefix(String name, String prefix) {
        return prefix + StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", name)));
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

    public static String fieldSourceLambda(String name) {
        return getFieldWithSuffix(name, "SourceLambda");
    }

    public static String fieldTargetLambda(String name) {
        return getFieldWithSuffix(name, "TargetLambda");
    }

    public String genGetter(String fieldName){
        return StrUtil.genGetter(StrUtil.toCamelCase(StrUtil.format("{}", fieldName)));
    }

}
