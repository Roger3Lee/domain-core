package com.artframework.domain.generator.table;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.FileUtils;
import com.artframework.domain.utils.NameUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "D:\\github\\domain-generator\\domain-generator\\src\\main\\java\\com\\artframework\\domain\\demo\\";
        TableGenerator generator = new TableGenerator("com.artframework.domain.demo");
        GlobalSetting.load("<tables>\n" +
                "    <table name=\"base\" basic=\"true\">\n" +
                "        <column name=\"id\" type=\"java.lang.Long\" comment=\"主鍵\" key=\"true\"/>\n" +
                "        <column name=\"uuid\" type=\"java.lang.String\" comment=\"唯一键\"/>\n" +
                "        <column name=\"gmt_create\" type=\"java.util.Date\" comment=\"創建時間\"/>\n" +
                "        <column name=\"create_user\" type=\"java.lang.String\" comment=\"創建用戶\"/>\n" +
                "    </table>\n" +
                "    <table name=\"user_info\" inherit=\"base\">\n" +
                "        <column name=\"name\" type=\"java.lang.String\" comment=\"名字\"/>\n" +
                "    </table>\n" +
                "    <table name=\"user_address\" inherit=\"base\">\n" +
                "        <column name=\"user_uuid\" type=\"java.lang.String\" comment=\"唯一键\"/>\n" +
                "        <column name=\"address_name\" type=\"java.lang.String\" comment=\"地址\"/>\n" +
                "    </table>\n" +
                "</tables>", "<domains>\n" +
                "    <domain name=\"user\" description=\"用戶域\" main-table=\"user_info\">\n" +
                "        <related table=\"user_address\" many=\"true\" fk=\"uuid:user_uuid\"/>\n" +
                "    </domain>\n" +
                "</domains>");
        List<TableMetaInfo> tableCollection = GlobalSetting.INSTANCE.getTableList();
        for (TableMetaInfo table : tableCollection) {
            TableInfo tableMetaInfo = TableInfo.covert(table);
            FileUtils.saveFile(path + "entities\\", NameUtils.dataObjectName(tableMetaInfo.getName())+ ".java", generator.generate(tableMetaInfo));
            if(!tableMetaInfo.getBasic()){
                MapperGenerator mapperGenerator = new MapperGenerator("com.artframework.domain.demo");
                FileUtils.saveFile(path + "mappers\\", NameUtils.mapperName(tableMetaInfo.getName()) + ".java", mapperGenerator.generate(tableMetaInfo));
            }
        }

    }
}
