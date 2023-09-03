package com.artframework.domain.generator.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.utils.FileUtils;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "D:\\github\\domain-generator\\domain-generator\\src\\main\\java\\com\\artframework\\domain\\demo\\domain\\";
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
                "</tables>","<domains>\n" +
                "    <domain name=\"user\" description=\"用戶域\" main-table=\"user_info\">\n" +
                "        <related table=\"user_address\" many=\"true\" fk=\"uuid:user_uuid\"/>\n" +
                "    </domain>\n" +
                "</domains>");
        for (DomainMetaInfo domainMetaInfo : GlobalSetting.INSTANCE.getDomainList()) {
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            DomainGenerator domainDtoGenerator = new DomainGenerator("com.artframework.domain.demo", FTLConstants.DTO_PATH);
            FileUtils.saveFile(path + "dto\\", domainInfo.nameSuffix("DTO") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.FIND_REQUEST_PATH);
            FileUtils.saveFile(path + "dto\\request\\", domainInfo.nameSuffix("FindRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.PAGE_REQUEST_PATH);
            FileUtils.saveFile(path + "dto\\request\\", domainInfo.nameSuffix("PageRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.UPDATE_REQUEST_PATH);
            FileUtils.saveFile(path + "dto\\request\\", domainInfo.nameSuffix("UpdateRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_PATH);
            FileUtils.saveFile(path + "repository\\", domainInfo.nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_PATH);
            FileUtils.saveFile(path + "repository\\impl\\", domainInfo.nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo));
        }

    }
}
