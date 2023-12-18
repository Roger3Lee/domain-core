package com.artframework.sample.domains.user.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.sample.domains.user.dto.*;
import com.artframework.sample.entities.*;

import java.util.function.*;
import java.io.Serializable;

/**
* user
*
* @author auto
* @version v1.0
* @date 2023-12-18 12:46:32
*/
@Getter
@Setter
@ToString
public class UserLambdaExp{
    /**
    * KEY  lambda
    */
    public static SFunction<UserDTO, Serializable> dtoKeyLambda= UserDTO::getId;

    /**
    * KEY  lambda
    */
    public static SFunction<UserInfoDO, Serializable> doKeyLambda= UserInfoDO::getId;


    /**
    *  user_address lambda
    */
    public static SFunction<UserDTO.UserAddressDTO, Serializable> userAddressKeyLambda = UserDTO.UserAddressDTO::getId;


    /**
    * RELATE user_address lambda
    */
    public static SFunction<UserDTO, Serializable> userAddressSourceLambda = UserDTO::getId;


    /**
    * RELATE user_address lambda
    */
    public static BiConsumer<UserDTO.UserAddressDTO,Integer> userAddressTargetSetLambda =UserDTO.UserAddressDTO::setUserId;

    /**
    * RELATE user_address lambda
    */
    public static SFunction<UserAddressDO,Serializable> userAddressTargetLambda =UserAddressDO::getUserId;

    /**
    *  user_family_member lambda
    */
    public static SFunction<UserDTO.UserFamilyMemberDTO, Serializable> userFamilyMemberKeyLambda = UserDTO.UserFamilyMemberDTO::getId;


    /**
    * RELATE user_family_member lambda
    */
    public static SFunction<UserDTO, Serializable> userFamilyMemberSourceLambda = UserDTO::getId;


    /**
    * RELATE user_address lambda
    */
    public static BiConsumer<UserDTO.UserFamilyMemberDTO,Integer> userFamilyMemberTargetSetLambda =UserDTO.UserFamilyMemberDTO::setUserId;

    /**
    * RELATE user_family_member lambda
    */
    public static SFunction<UserFamilyMemberDO,Serializable> userFamilyMemberTargetLambda =UserFamilyMemberDO::getUserId;

}
