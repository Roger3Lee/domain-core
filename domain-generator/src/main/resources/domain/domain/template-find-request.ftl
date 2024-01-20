package ${domainPackage!''}.${NameUtils.packageName(source.folder)}.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ${NameUtils.getName(source.name)}FindDomain {
<#--    private ${source.mainTable.keyType} key;-->
    private Serializable key;

    /**
    * 默认加载所有
    */
    @Builder.Default
    private ${NameUtils.dataTOName(source.name)}.LoadFlag loadFlag = new ${NameUtils.dataTOName(source.name)}.LoadFlag();
}
