
package com.artframework.domain.web.generator.domain.ddd.convertor;

import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.dataobject.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = DDDConvertorDecorator.class)
public interface  DDDConvertor{
    DDDConvertor INSTANCE= Mappers.getMapper(DDDConvertor.class);

    @Mapping(target = "domainConfigTablesList", ignore = true)
    @Mapping(target = "domainConfigLineList", ignore = true)
    @Mapping(target = "domainConfigLineConfigList", ignore = true)
    DDDDomain copy(DDDDomain request);
    DDDDomain convert2DTO(DomainConfigDO request);
    void convert2DTO(DomainConfigDO request, @MappingTarget DDDDomain target);
    List<DDDDomain> convert2DTO(List<DomainConfigDO> request);


    @BeanMapping(qualifiedByName = { "DDDConvertorDecorator"})
    DomainConfigDO convert2DO(DDDDomain request);
    List<DomainConfigDO> convert2DO(List<DDDDomain> request);

    DDDDomain.DomainConfigTablesDomain convert2DomainConfigTablesDTO(DomainConfigTablesDO request);
    void convert2DomainConfigTablesDTO(DomainConfigTablesDO request, @MappingTarget DDDDomain.DomainConfigTablesDomain target);
    List<DDDDomain.DomainConfigTablesDomain> convert2DomainConfigTablesDTO(List<DomainConfigTablesDO>  request);
    @BeanMapping(qualifiedByName = { "DDDConvertorDecorator"})
    DomainConfigTablesDO convert2DomainConfigTablesDO(DDDDomain.DomainConfigTablesDomain request);
    List<DomainConfigTablesDO> convert2DomainConfigTablesDO(List<DDDDomain.DomainConfigTablesDomain>  request);
    DDDDomain.DomainConfigLineDomain convert2DomainConfigLineDTO(DomainConfigLineDO request);
    void convert2DomainConfigLineDTO(DomainConfigLineDO request, @MappingTarget DDDDomain.DomainConfigLineDomain target);
    List<DDDDomain.DomainConfigLineDomain> convert2DomainConfigLineDTO(List<DomainConfigLineDO>  request);
    @BeanMapping(qualifiedByName = { "DDDConvertorDecorator"})
    DomainConfigLineDO convert2DomainConfigLineDO(DDDDomain.DomainConfigLineDomain request);
    List<DomainConfigLineDO> convert2DomainConfigLineDO(List<DDDDomain.DomainConfigLineDomain>  request);
    DDDDomain.DomainConfigLineConfigDomain convert2DomainConfigLineConfigDTO(DomainConfigLineConfigDO request);
    void convert2DomainConfigLineConfigDTO(DomainConfigLineConfigDO request, @MappingTarget DDDDomain.DomainConfigLineConfigDomain target);
    List<DDDDomain.DomainConfigLineConfigDomain> convert2DomainConfigLineConfigDTO(List<DomainConfigLineConfigDO>  request);
    @BeanMapping(qualifiedByName = { "DDDConvertorDecorator"})
    DomainConfigLineConfigDO convert2DomainConfigLineConfigDO(DDDDomain.DomainConfigLineConfigDomain request);
    List<DomainConfigLineConfigDO> convert2DomainConfigLineConfigDO(List<DDDDomain.DomainConfigLineConfigDomain>  request);
}
