package com.artframework.domain.core.lambda;

import com.artframework.domain.core.MPFieldLambda;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用於緩存lambda表達式的信息
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/4
 **/
public class LambdaCache {

    private static final Map<Class<?>, Map<String, MPFieldLambda.SSFunction<?, Serializable>>> LAMBDA_CACHE = new ConcurrentHashMap<>();
    /**
     * 獲取lambda的信息
     * @param column
     * @return
     * @param <D>
     */
    public static <D> LambdaInfo<D> info(SFunction<D, Serializable> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return new LambdaInfo(meta.getInstantiatedClass(), meta.getInstantiatedClass().getCanonicalName(), fieldName);
    }


    public static <DO, D> MPFieldLambda.SSFunction<DO, Serializable> DOLambda(Class<DO> doClass, SFunction<D, Serializable> column) {
        LambdaInfo<D> lambdaInfo = LambdaCache.info(column);
        return LambdaCache.DOLambda(doClass, lambdaInfo.getFieldName());
    }

    public static <DO> MPFieldLambda.SSFunction<DO, Serializable> DOLambda(Class<DO> doClass, String field) {
        return (MPFieldLambda.SSFunction<DO, Serializable>) LAMBDA_CACHE.computeIfAbsent(doClass, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(field, k -> MPFieldLambda.fieldLambda(doClass, field));
    }

    @Data
    @AllArgsConstructor
    public static class LambdaInfo<D> {
        private Class<D> clazz;
        private String clazzName;
        private String fieldName;
    }
}
