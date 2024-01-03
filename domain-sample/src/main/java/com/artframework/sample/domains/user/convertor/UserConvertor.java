
package com.artframework.sample.domains.user.convertor;

import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.entities.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = UserConvertorDecorator.class)
public interface  UserConvertor{
    UserConvertor INSTANCE= Mappers.getMapper(UserConvertor.class);

    UserDomain convert2DTO(UserInfoDO request);
    List<UserDomain> convert2DTO(List<UserInfoDO> request);

    @BeanMapping(qualifiedByName = { "UserConvertorDecorator"})
    UserInfoDO convert2DO(UserDomain request);
    List<UserInfoDO> convert2DO(List<UserDomain> request);

    UserDomain.UserAddressDomain convert2UserAddressDTO(UserAddressDO request);
    List<UserDomain.UserAddressDomain> convert2UserAddressDTO(List<UserAddressDO>  request);
    UserAddressDO convert2UserAddressDO(UserDomain.UserAddressDomain request);
    List<UserAddressDO> convert2UserAddressDO(List<UserDomain.UserAddressDomain>  request);
    UserDomain.UserFamilyMemberDomain convert2UserFamilyMemberDTO(UserFamilyMemberDO request);
    List<UserDomain.UserFamilyMemberDomain> convert2UserFamilyMemberDTO(List<UserFamilyMemberDO>  request);
    UserFamilyMemberDO convert2UserFamilyMemberDO(UserDomain.UserFamilyMemberDomain request);
    List<UserFamilyMemberDO> convert2UserFamilyMemberDO(List<UserDomain.UserFamilyMemberDomain>  request);

}
