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

    public static String getListFieldName(String name) {
        return StringUtils.uncapitalize(StrUtil.toCamelCase(name)) + "List";
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
        return getNameWithSuffix(name, "Domain");
    }

    public static String mapperName(String name) {
        return getNameWithSuffix(name, "Mapper");
    }

    public static String repositoryName(String name) {
        return getNameWithSuffix(name, "Repository");
    }

    public static String repositoryImplName(String name) {
        return getNameWithSuffix(name, "RepositoryImpl");
    }

    public static String serviceName(String name) {
        return getNameWithSuffix(name, "Service");
    }

    public static String controllerName(String name) {
        return getNameWithSuffix(name, "Controller");
    }

    public static String serviceImplName(String name) {
        return getNameWithSuffix(name, "ServiceImpl");
    }

    public static String lambdaExpName(String name) {
        return getNameWithSuffix(name, "LambdaExp");
    }

    public static String fieldSourceLambda(String name) {
        return getFieldWithSuffix(name, "DomainEntitySourceLambda");
    }


    public static String fieldTargetKeyLambda(String name) {
        return getFieldWithSuffix(name, "DomainKeyLambda");
    }

    public static String fieldTargetLambda(String name) {
        return getFieldWithSuffix(name, "DOTargetLambda");
    }

    public static String fieldTargetDomainLambda(String name) {
        return getFieldWithSuffix(name, "DomainTargetLambda");
    }

    public static String fieldTargetSetLambda(String name) {
        return getFieldWithSuffix(name, "DomainTargetSetLambda");
    }

    public static String covertName(String name) {
        return getNameWithSuffix(name, "Convertor");
    }

    public static String covertDecoratorName(String name) {
        return getNameWithSuffix(name, "ConvertorDecorator");
    }

    public static String packageName(String name) {
        return StrUtil.toCamelCase(StrUtil.format("{}", name)).toLowerCase();
    }

    public String genGetter(String fieldName) {
        return StrUtil.genGetter(StrUtil.toCamelCase(StrUtil.format("{}", fieldName)));
    }

    public String genListGetter(String fieldName) {
        return StrUtil.genGetter(StrUtil.toCamelCase(StrUtil.format("{}", fieldName))) + "List";
    }

    public String genSetter(String fieldName) {
        return StrUtil.genSetter(StrUtil.toCamelCase(StrUtil.format("{}", fieldName)));
    }

    public String genListSetter(String fieldName) {
        return StrUtil.genSetter(StrUtil.toCamelCase(StrUtil.format("{}", fieldName))) + "List";
    }
}
