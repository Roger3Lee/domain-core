package ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.request;

import lombok.*;

import java.io.Serializable;

import ${basePackage!''}.domain.${NameUtils.packageName(source.name)}.dto.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ${NameUtils.getName(source.name)}FindRequest {
<#--    private ${source.mainTable.keyType} key;-->
    private Serializable key;

    /**
    * 默认加载所有
    */
    private ${NameUtils.dataTOName(source.name)}.LoadFlag loadFlag = new ${NameUtils.dataTOName(source.name)}.LoadFlag();
}
