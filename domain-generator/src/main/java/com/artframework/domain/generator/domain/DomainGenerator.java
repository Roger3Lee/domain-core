package com.artframework.domain.generator.domain;

import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.generator.AbstractGenerator;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import org.springframework.stereotype.Component;

@Component
public class DomainGenerator extends AbstractGenerator {
    public DomainGenerator(){
        this.basePackage = "";
    }

    @Override
    public <T> Object buildParam(T source) {
        if(source instanceof DomainMetaInfo){
            return DomainInfo.covert((DomainMetaInfo) source);
        }
        return source;
    }
}
