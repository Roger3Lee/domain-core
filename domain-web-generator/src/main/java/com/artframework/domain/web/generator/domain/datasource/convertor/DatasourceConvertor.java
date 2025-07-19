
package com.artframework.domain.web.generator.domain.datasource.convertor;

import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.dataobject.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = DatasourceConvertorDecorator.class)
public interface  DatasourceConvertor{
    DatasourceConvertor INSTANCE= Mappers.getMapper(DatasourceConvertor.class);

    @Mapping(target = "datasourceTableList", ignore = true)
    @Mapping(target = "datasourceTableColumnList", ignore = true)
    DatasourceDomain copy(DatasourceDomain request);
    DatasourceDomain convert2DTO(DatasourceConfigDO request);
    void convert2DTO(DatasourceConfigDO request, @MappingTarget DatasourceDomain target);
    List<DatasourceDomain> convert2DTO(List<DatasourceConfigDO> request);


    @BeanMapping(qualifiedByName = { "DatasourceConvertorDecorator"})
    DatasourceConfigDO convert2DO(DatasourceDomain request);
    List<DatasourceConfigDO> convert2DO(List<DatasourceDomain> request);

    DatasourceDomain.DatasourceTableDomain convert2DatasourceTableDTO(DatasourceTableDO request);
    void convert2DatasourceTableDTO(DatasourceTableDO request, @MappingTarget DatasourceDomain.DatasourceTableDomain target);
    List<DatasourceDomain.DatasourceTableDomain> convert2DatasourceTableDTO(List<DatasourceTableDO>  request);
    @BeanMapping(qualifiedByName = { "DatasourceConvertorDecorator"})
    DatasourceTableDO convert2DatasourceTableDO(DatasourceDomain.DatasourceTableDomain request);
    List<DatasourceTableDO> convert2DatasourceTableDO(List<DatasourceDomain.DatasourceTableDomain>  request);
    DatasourceDomain.DatasourceTableColumnDomain convert2DatasourceTableColumnDTO(DatasourceTableColumnDO request);
    void convert2DatasourceTableColumnDTO(DatasourceTableColumnDO request, @MappingTarget DatasourceDomain.DatasourceTableColumnDomain target);
    List<DatasourceDomain.DatasourceTableColumnDomain> convert2DatasourceTableColumnDTO(List<DatasourceTableColumnDO>  request);
    @BeanMapping(qualifiedByName = { "DatasourceConvertorDecorator"})
    DatasourceTableColumnDO convert2DatasourceTableColumnDO(DatasourceDomain.DatasourceTableColumnDomain request);
    List<DatasourceTableColumnDO> convert2DatasourceTableColumnDO(List<DatasourceDomain.DatasourceTableColumnDomain>  request);
}
