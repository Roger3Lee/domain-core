package com.artframework.domain.generator;

public class CommonGenerator extends AbstractGenerator{

    public CommonGenerator(String basePackage) {
        this.basePackage = basePackage;
    }
    @Override
    public <T> Object buildParam(T source) {
        return source;
    }
}
