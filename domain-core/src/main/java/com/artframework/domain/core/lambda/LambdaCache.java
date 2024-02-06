package com.artframework.domain.core.lambda;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.artframework.domain.core.MPFieldLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;

/**
 * 用於緩存lambda表達式的信息
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/4
 **/
public class LambdaCache {


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


    public static <DO, D, R> MPFieldLambda.SSFunction<DO, R> DOLambda(Class<DO> doClass, SFunction<D, Serializable> column) {
        LambdaInfo<D> lambdaInfo = LambdaCache.info(column);
        return LambdaCache.DOLambda(doClass, lambdaInfo.getFieldName());
    }

    public static <DO, R> MPFieldLambda.SSFunction<DO, R> DOLambda(Class<DO> doClass, String field) {
        return MPFieldLambda.fieldLambda(doClass, field);
    }

    @Data
    @AllArgsConstructor
    public static class LambdaInfo<D> {
        private Class<D> clazz;
        private String clazzName;
        private String fieldName;
    }
}
