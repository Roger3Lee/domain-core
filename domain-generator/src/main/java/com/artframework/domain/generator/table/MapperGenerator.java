package com.artframework.domain.generator.table;

import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.generator.AbstractGenerator;
import com.artframework.domain.meta.table.TableMetaInfo;

public class MapperGenerator extends AbstractGenerator {

    public MapperGenerator(String basePackage) {
        this.basePackage = basePackage;
        this.templateFilePath = "table/mapper/template-mapper.ftl";
    }
    @Override
    public <T> Object buildParam(T source) {
        if(source instanceof TableMetaInfo){
            return TableInfo.covert((TableMetaInfo) source);
        }
        return source;
    }
}
