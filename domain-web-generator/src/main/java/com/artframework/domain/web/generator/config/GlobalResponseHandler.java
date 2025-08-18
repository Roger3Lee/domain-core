package com.artframework.domain.web.generator.config;

import com.artframework.domain.web.generator.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理器
 * 自动包装所有控制器的返回值到ApiResponse中
 * 只对web-generator包下的控制器生效
 * 
 * @author auto
 * @version v1.0
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.artframework.domain.web.generator")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回值已经是ApiResponse类型，则不进行包装
        return !ApiResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request, ServerHttpResponse response) {
        
        // 如果返回值为null，包装为成功响应
        if (body == null) {
            return ApiResponse.success();
        }
        
        // 包装业务数据到ApiResponse中
        return ApiResponse.success(body);
    }
}
