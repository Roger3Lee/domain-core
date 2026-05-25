package io.github.roger3lee.domain.generator.domain;

import io.github.roger3lee.domain.dto.DomainInfo;
import io.github.roger3lee.domain.generator.AbstractGenerator;
import io.github.roger3lee.domain.meta.domain.DomainMetaInfo;

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
