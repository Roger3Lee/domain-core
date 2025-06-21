package com.artframework.domain.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具类
 *
 * @author fxz
 */
public class GenericsUtils {

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     *         determined
     */
    public static Class getSuperClassGenericType(final Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz The class to introspect
     * @param index the Index of the generic declaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     *         determined
     */
    public static Class getSuperClassGenericType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }

        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

}