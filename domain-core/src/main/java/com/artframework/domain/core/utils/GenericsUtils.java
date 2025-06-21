package com.artframework.domain.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 * 提供通过反射获取泛型类型信息的工具方法
 *
 * @author fxz
 */
@Slf4j
public class GenericsUtils {

    /**
     * 通过反射获取类的父类第一个泛型参数类型
     * 如无法找到，返回 Object.class
     *
     * @param clazz 要分析的类
     * @return 第一个泛型参数类型，如果无法确定则返回 Object.class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenericType(final Class<?> clazz) {
        return (Class<T>) getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射获取类的父类指定位置的泛型参数类型
     * 如无法找到，返回 Object.class
     *
     * @param clazz 要分析的类
     * @param index 泛型参数的索引位置，从0开始
     * @return 指定位置的泛型参数类型，如果无法确定则返回 Object.class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenericType(final Class<?> clazz, final int index) {
        if (clazz == null) {
            log.warn("传入的类为空，返回 Object.class");
            return (Class<T>) Object.class;
        }

        if (index < 0) {
            log.warn("泛型参数索引不能为负数: {}, 返回 Object.class", index);
            return (Class<T>) Object.class;
        }

        Type genType = clazz.getGenericSuperclass();

        // 检查是否为参数化类型
        if (!(genType instanceof ParameterizedType)) {
            log.debug("类 {} 的父类不是参数化类型，返回 Object.class", clazz.getName());
            return (Class<T>) Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        // 检查索引是否有效
        if (index >= params.length) {
            log.warn("泛型参数索引 {} 超出范围，最大索引为 {}，返回 Object.class",
                    index, params.length - 1);
            return (Class<T>) Object.class;
        }

        Type targetType = params[index];

        // 检查是否为 Class 类型
        if (!(targetType instanceof Class)) {
            log.debug("泛型参数 {} 不是 Class 类型，而是 {}，返回 Object.class",
                    index, targetType.getClass().getSimpleName());
            return (Class<T>) Object.class;
        }

        Class<T> result = (Class<T>) targetType;
        log.debug("成功获取类 {} 的第 {} 个泛型参数类型: {}",
                clazz.getName(), index, result.getName());

        return result;
    }

    /**
     * 获取类的所有父类泛型参数类型
     * 
     * @param clazz 要分析的类
     * @return 泛型参数类型数组，如果无法确定则返回空数组
     */
    public static Class<?>[] getAllSuperClassGenericTypes(final Class<?> clazz) {
        if (clazz == null) {
            return new Class<?>[0];
        }

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return new Class<?>[0];
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Class<?>[] result = new Class<?>[params.length];

        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof Class) {
                result[i] = (Class<?>) params[i];
            } else {
                result[i] = Object.class;
            }
        }

        return result;
    }

    /**
     * 检查类是否具有泛型父类
     * 
     * @param clazz 要检查的类
     * @return 如果有泛型父类返回 true，否则返回 false
     */
    public static boolean hasGenericSuperClass(final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        Type genType = clazz.getGenericSuperclass();
        return genType instanceof ParameterizedType;
    }

    /**
     * 获取泛型父类的泛型参数数量
     * 
     * @param clazz 要分析的类
     * @return 泛型参数数量，如果不是泛型类则返回 0
     */
    public static int getGenericSuperClassParameterCount(final Class<?> clazz) {
        if (!hasGenericSuperClass(clazz)) {
            return 0;
        }

        Type genType = clazz.getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return params.length;
    }
}