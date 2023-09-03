package com.artframework.domain.generator.table;

import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.meta.table.TableCollection;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.FileUtils;
import com.artframework.domain.utils.NameUtils;
import com.artframework.domain.utils.XmlUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "D:\\github\\domain-generator\\domain-generator\\src\\main\\java\\com\\artframework\\domain\\demo\\";
        TableGenerator generator = new TableGenerator("com.artframework.domain.demo");
        TableCollection tableCollection= XmlUtils.xmlToBean("<tables>\n" +
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
                "</tables>",TableCollection.class);

        for (TableMetaInfo table : tableCollection.getTables()) {
            TableInfo tableMetaInfo = TableInfo.covert(table);
            FileUtils.saveFile(path + "entities\\", NameUtils.dataObjectName(tableMetaInfo.getName())+ ".java", generator.generate(tableMetaInfo));
            if(!tableMetaInfo.getBasic()){
                MapperGenerator mapperGenerator = new MapperGenerator("com.artframework.domain.demo");
                FileUtils.saveFile(path + "mappers\\", NameUtils.mapperName(tableMetaInfo.getName()) + ".java", mapperGenerator.generate(tableMetaInfo));
            }
        }

    }
}
