package com.artframework.domain.core;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/11
 *
 * 此類用於動態生成Mybatis plus可以識別的getter方法的lambda表達式， 主要用於通過字段名來過濾數據
 *
 **/
@Data
public class MPFieldLambda<T> {
    private Class<T> zClass;
    private String fieldName;

    private  MPFieldLambda(Class<T> doClass, String key) {
        this.zClass = doClass;
        this.fieldName = key;
    }
    @SneakyThrows
    private <T, R> SSFunction<T, R> fieldLambda(){
        Field field = ReflectUtil.getField(zClass, this.fieldName);
        if (null == field) {
            throw new NoSuchFieldException(this.fieldName);
        }

        String instantiatedMethodType = String.format("(L%s;)L%s", zClass.getName(), field.getType().getName());
        return new SSFunction<T, R>() {
            @Override
            public R apply(T t) {
                return null;
            }

            @Override
            public Object writeReplace() {
                return new SerializedLambda(zClass, null, null, null, 0
                        , zClass.getName(), CharSequenceUtil.genGetter(fieldName), null, instantiatedMethodType, new Object[0]);
            }
        };
    }

    public static <T, R> SSFunction<T, R> fieldLambda(Class<T> doClass, String key) {
        MPFieldLambda fieldLambda = new MPFieldLambda(doClass, key);
        return fieldLambda.fieldLambda();
    }

    @FunctionalInterface
    public interface SSFunction<T, R> extends SFunction<T, R> {

        default Object writeReplace() {
            return null;
        }
    }
}
