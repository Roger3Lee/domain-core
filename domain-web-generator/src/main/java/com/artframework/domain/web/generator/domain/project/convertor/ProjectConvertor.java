
package com.artframework.domain.web.generator.domain.project.convertor;

import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.dataobject.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = ProjectConvertorDecorator.class)
public interface  ProjectConvertor{
    ProjectConvertor INSTANCE= Mappers.getMapper(ProjectConvertor.class);

    @Mapping(target = "domainConfigList", ignore = true)
    ProjectDomain copy(ProjectDomain request);
    ProjectDomain convert2DTO(ProjectDO request);
    void convert2DTO(ProjectDO request, @MappingTarget ProjectDomain target);
    List<ProjectDomain> convert2DTO(List<ProjectDO> request);


    @BeanMapping(qualifiedByName = { "ProjectConvertorDecorator"})
    ProjectDO convert2DO(ProjectDomain request);
    List<ProjectDO> convert2DO(List<ProjectDomain> request);

    ProjectDomain.DomainConfigDomain convert2DomainConfigDTO(DomainConfigDO request);
    void convert2DomainConfigDTO(DomainConfigDO request, @MappingTarget ProjectDomain.DomainConfigDomain target);
    List<ProjectDomain.DomainConfigDomain> convert2DomainConfigDTO(List<DomainConfigDO>  request);
    @BeanMapping(qualifiedByName = { "ProjectConvertorDecorator"})
    DomainConfigDO convert2DomainConfigDO(ProjectDomain.DomainConfigDomain request);
    List<DomainConfigDO> convert2DomainConfigDO(List<ProjectDomain.DomainConfigDomain>  request);
}
