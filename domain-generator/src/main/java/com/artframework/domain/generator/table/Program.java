package com.artframework.domain.generator.table;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.generator.CommonGenerator;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.FileUtils;
import com.artframework.domain.utils.NameUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "D:\\github\\domain-generator\\domain-generator\\src\\main\\java\\com\\artframework\\domain\\demo\\";
        TableGenerator generator = new TableGenerator("com.artframework.domain.demo");
        GlobalSetting.load(new File("D:\\github\\domain-generator\\config\\table-list.xml"),
                new File("D:\\github\\domain-generator\\config\\domain-config.xml"));

        CommonGenerator commonGenerator=new CommonGenerator("com.artframework.domain.demo");
        commonGenerator.setTemplateFilePath(FTLConstants.COMMON_PAGE_REQUEST);
        FileUtils.saveFile(path + "common\\", "PageRequest.java", commonGenerator.generate(null));

        List<TableMetaInfo> tableCollection = GlobalSetting.INSTANCE.getTableList();
        for (TableMetaInfo table : tableCollection) {
            TableInfo tableMetaInfo = TableInfo.covert(table);
            generator.setTemplateFilePath(FTLConstants.TABLE_DO_PATH);
            FileUtils.saveFile(path + "entities\\", NameUtils.dataObjectName(tableMetaInfo.getName())+ ".java", generator.generate(tableMetaInfo));
            if(!tableMetaInfo.getBasic()){
                generator.setTemplateFilePath(FTLConstants.TABLE_MAPPER_PATH);
                FileUtils.saveFile(path + "mappers\\", NameUtils.mapperName(tableMetaInfo.getName()) + ".java", generator.generate(tableMetaInfo));
            }
        }

    }
}
