package com.artframework.domain.generator.table;

import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.generator.AbstractGenerator;
import org.springframework.stereotype.Component;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
@Component
public class TableGenerator extends AbstractGenerator {

    public TableGenerator() {
        this.basePackage = "";
        this.templateFilePath = "table/template-table.ftl";
    }

    @Override
    public <T> Object buildParam(T source) {
        if(source instanceof TableMetaInfo){
            return TableInfo.covert((TableMetaInfo) source);
        }
        return source;
    }
}

