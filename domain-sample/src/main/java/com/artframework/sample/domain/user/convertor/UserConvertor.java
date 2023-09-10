
package com.artframework.sample.domain.user.convertor;

import com.artframework.sample.domain.user.dto.*;
import com.artframework.sample.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface  UserConvertor{
    UserConvertor INSTANCE= Mappers.getMapper(UserConvertor.class);

    UserDTO convert2DTO(UserInfoDO request);
    List<UserDTO> convert2DTO(List<UserInfoDO> request);
    UserInfoDO convert2DO(UserDTO request);
    List<UserInfoDO> convert2DO(List<UserDTO> request);

    UserDTO.UserAddressDTO convert2UserAddressDTO(UserAddressDO request);
    List<UserDTO.UserAddressDTO> convert2UserAddressDTO(List<UserAddressDO>  request);
    UserAddressDO convert2UserAddressDO(UserDTO.UserAddressDTO request);
    List<UserAddressDO> convert2UserAddressDO(List<UserDTO.UserAddressDTO>  request);
    UserDTO.UserFamilyMemberDTO convert2UserFamilyMemberDTO(UserFamilyMemberDO request);
    List<UserDTO.UserFamilyMemberDTO> convert2UserFamilyMemberDTO(List<UserFamilyMemberDO>  request);
    UserFamilyMemberDO convert2UserFamilyMemberDO(UserDTO.UserFamilyMemberDTO request);
    List<UserFamilyMemberDO> convert2UserFamilyMemberDO(List<UserDTO.UserFamilyMemberDTO>  request);
}
