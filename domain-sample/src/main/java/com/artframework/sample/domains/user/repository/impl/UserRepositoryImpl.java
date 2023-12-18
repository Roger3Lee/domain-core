package com.artframework.sample.domains.user.repository.impl;

import com.artframework.sample.domains.user.convertor.*;
import com.artframework.sample.domains.user.lambdaexp.*;
import com.artframework.sample.domains.user.dto.*;
import com.artframework.sample.domains.user.dto.request.*;
import com.artframework.sample.domains.user.repository.*;
import com.artframework.sample.entities.*;
import com.artframework.domain.core.repository.impl.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<UserDTO,UserInfoDO>  implements UserRepository {

    @Override
    public List<UserInfoDO> convert2DO(List<UserDTO> list) {
        return UserConvertor.INSTANCE.convert2DO(list);
    }

    @Override
    public List<UserDTO> convert2DTO(List<UserInfoDO> list) {
        return UserConvertor.INSTANCE.convert2DTO(list);
    }

    @Override
    public SFunction<UserDTO, Serializable> keyLambda() {
        return UserLambdaExp.dtoKeyLambda;
    }

    @Override
    public Class<UserInfoDO> getDOClass() {
        return UserInfoDO.class;
    }

    @Override
    public IPage<UserDTO> page(UserPageRequest request){
        IPage<UserInfoDO> page=new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<UserInfoDO> wrapper =new LambdaQueryWrapper<UserInfoDO>();
        return this.baseMapper.selectPage(page,wrapper).convert(UserConvertor.INSTANCE::convert2DTO);
    }

    @Repository
    public static class UserAddressRepositoryImpl extends BaseRepositoryImpl<UserDTO.UserAddressDTO,UserAddressDO>  implements UserAddressRepository {

        @Override
        public List<UserAddressDO> convert2DO(List<UserDTO.UserAddressDTO> list) {
            return UserConvertor.INSTANCE.convert2UserAddressDO(list);
        }

        @Override
        public List<UserDTO.UserAddressDTO> convert2DTO(List<UserAddressDO> list) {
            return UserConvertor.INSTANCE.convert2UserAddressDTO(list);
        }

        @Override
        public SFunction<UserDTO.UserAddressDTO, Serializable> keyLambda() {
            return UserLambdaExp.userAddressKeyLambda;
        }

        @Override
        public Class<UserAddressDO> getDOClass() {
            return UserAddressDO.class;
        }
    }
    @Repository
    public static class UserFamilyMemberRepositoryImpl extends BaseRepositoryImpl<UserDTO.UserFamilyMemberDTO,UserFamilyMemberDO>  implements UserFamilyMemberRepository {

        @Override
        public List<UserFamilyMemberDO> convert2DO(List<UserDTO.UserFamilyMemberDTO> list) {
            return UserConvertor.INSTANCE.convert2UserFamilyMemberDO(list);
        }

        @Override
        public List<UserDTO.UserFamilyMemberDTO> convert2DTO(List<UserFamilyMemberDO> list) {
            return UserConvertor.INSTANCE.convert2UserFamilyMemberDTO(list);
        }

        @Override
        public SFunction<UserDTO.UserFamilyMemberDTO, Serializable> keyLambda() {
            return UserLambdaExp.userFamilyMemberKeyLambda;
        }

        @Override
        public Class<UserFamilyMemberDO> getDOClass() {
            return UserFamilyMemberDO.class;
        }
    }

}
