package com.artframework.domain.generator.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DomainDtoGenerator extends DomainAbstractGenerator {
    public DomainDtoGenerator(String basePackage){
        this.basePackage = basePackage;
        this.templateFilePath = "domain/dto/template-dto.ftl";
    }
}
