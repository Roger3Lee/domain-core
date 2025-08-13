package com.artframework.domain.web.generator.exception;

/**
 * 业务异常类
 * 
 * @author auto
 * @version v1.0
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}


