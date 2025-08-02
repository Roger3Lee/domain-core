package com.artframework.domain.web.generator.service.convert;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 领域模型应用服务转换器
 * 用于处理请求体到领域实体的双向转换
 * 
 * @author auto
 * @version v1.0
 */
@Mapper(componentModel = "spring")
public interface DomainConfigAppConvertor {
    
    DomainConfigAppConvertor INSTANCE = Mappers.getMapper(DomainConfigAppConvertor.class);

    /**
     * 请求DTO转领域对象
     */
    DDDDomain toDomain(DomainConfigAddRequest request);
    
    /**
     * 请求DTO转领域对象
     */
    DDDDomain toDomain(DomainConfigEditRequest request);
    
    /**
     * 领域对象转响应DTO
     */
    DomainConfigResponse toResponse(DDDDomain domain);
    
    /**
     * 领域对象列表转响应DTO列表
     */
    List<DomainConfigResponse> toResponseList(List<DDDDomain> domainList);
} 