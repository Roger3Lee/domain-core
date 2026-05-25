
package com.artframework.sample.domains.family.convertor;

import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.entities.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = FamilyConvertorDecorator.class)
public interface  FamilyConvertor{
    FamilyConvertor INSTANCE= Mappers.getMapper(FamilyConvertor.class);

    @Mapping(target = "familyAddress", ignore = true)
    @Mapping(target = "familyMemberList", ignore = true)
    FamilyDomain copy(FamilyDomain request);
    FamilyDomain convert2DTO(FamilyDO request);
    void convert2DTO(FamilyDO request, @MappingTarget FamilyDomain target);
    List<FamilyDomain> convert2DTO(List<FamilyDO> request);


    @BeanMapping(qualifiedByName = { "FamilyConvertorDecorator"})
    FamilyDO convert2DO(FamilyDomain request);
    List<FamilyDO> convert2DO(List<FamilyDomain> request);

    FamilyDomain.FamilyAddressDomain convert2FamilyAddressDTO(FamilyAddressDO request);
    void convert2FamilyAddressDTO(FamilyAddressDO request, @MappingTarget FamilyDomain.FamilyAddressDomain target);
    List<FamilyDomain.FamilyAddressDomain> convert2FamilyAddressDTO(List<FamilyAddressDO>  request);
    @BeanMapping(qualifiedByName = { "FamilyConvertorDecorator"})
    FamilyAddressDO convert2FamilyAddressDO(FamilyDomain.FamilyAddressDomain request);
    List<FamilyAddressDO> convert2FamilyAddressDO(List<FamilyDomain.FamilyAddressDomain>  request);
    FamilyDomain.FamilyMemberDomain convert2FamilyMemberDTO(FamilyMemberDO request);
    void convert2FamilyMemberDTO(FamilyMemberDO request, @MappingTarget FamilyDomain.FamilyMemberDomain target);
    List<FamilyDomain.FamilyMemberDomain> convert2FamilyMemberDTO(List<FamilyMemberDO>  request);
    @BeanMapping(qualifiedByName = { "FamilyConvertorDecorator"})
    FamilyMemberDO convert2FamilyMemberDO(FamilyDomain.FamilyMemberDomain request);
    List<FamilyMemberDO> convert2FamilyMemberDO(List<FamilyDomain.FamilyMemberDomain>  request);
}
