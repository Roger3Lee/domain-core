package io.github.roger3lee.domain.generator.table;

import io.github.roger3lee.domain.dto.TableInfo;
import io.github.roger3lee.domain.generator.AbstractGenerator;
import io.github.roger3lee.domain.meta.table.TableMetaInfo;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/8/30
 **/
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

