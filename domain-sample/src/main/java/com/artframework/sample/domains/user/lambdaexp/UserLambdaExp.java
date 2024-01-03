package com.artframework.sample.domains.user.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.sample.domains.user.domain.*;
import com.artframework.sample.entities.*;

import java.util.function.*;
import java.io.Serializable;

/**
* user
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
public class UserLambdaExp{
    /**
    * KEY  lambda
    */
    public static SFunction<UserDomain, Serializable> dtoKeyLambda= UserDomain::getId;

    /**
    * KEY  lambda
    */
    public static SFunction<UserInfoDO, Serializable> doKeyLambda= UserInfoDO::getId;


    /**
    *  user_address lambda
    */
    public static SFunction<UserDomain.UserAddressDomain, Serializable> userAddressKeyLambda = UserDomain.UserAddressDomain::getId;


    /**
    * RELATE user_address lambda
    */
    public static SFunction<UserDomain, Serializable> userAddressSourceLambda = UserDomain::getId;


    /**
    * RELATE user_address lambda
    */
    public static BiConsumer<UserDomain.UserAddressDomain,java.lang.Long> userAddressTargetSetLambda =UserDomain.UserAddressDomain::setUserId;

    /**
    * RELATE user_address lambda
    */
    public static SFunction<UserAddressDO,Serializable> userAddressTargetLambda =UserAddressDO::getUserId;

    /**
    *  user_family_member lambda
    */
    public static SFunction<UserDomain.UserFamilyMemberDomain, Serializable> userFamilyMemberKeyLambda = UserDomain.UserFamilyMemberDomain::getId;


    /**
    * RELATE user_family_member lambda
    */
    public static SFunction<UserDomain, Serializable> userFamilyMemberSourceLambda = UserDomain::getId;


    /**
    * RELATE user_address lambda
    */
    public static BiConsumer<UserDomain.UserFamilyMemberDomain,java.lang.Long> userFamilyMemberTargetSetLambda =UserDomain.UserFamilyMemberDomain::setUserId;

    /**
    * RELATE user_family_member lambda
    */
    public static SFunction<UserFamilyMemberDO,Serializable> userFamilyMemberTargetLambda =UserFamilyMemberDO::getUserId;

}
