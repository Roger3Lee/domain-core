package com.artframework.sample.domains.family.lambdaexp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.entities.*;

import java.util.function.*;
import java.io.Serializable;

/**
* family
*
* @author auto
* @version v1.0
*/
@Getter
@Setter
@ToString
public class FamilyLambdaExp{
    /**
    * KEY family lambda
    */
    public static SFunction<FamilyDomain, Serializable> dtoKeyLambda= FamilyDomain::getId;

    /**
    * KEY family lambda
    */
    public static SFunction<FamilyDO, Serializable> doKeyLambda= FamilyDO::getId;


    /**
    *  family_address lambda
    */
    public static SFunction<FamilyDomain.FamilyAddressDomain, Serializable> familyAddressDomainKeyLambda = FamilyDomain.FamilyAddressDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<FamilyDomain, Serializable> familyId_RelatedFamilyAddress_SourceLambda = FamilyDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<FamilyDomain.FamilyAddressDomain,Serializable> familyAddress_familyIdTargetLambda =FamilyDomain.FamilyAddressDomain::getFamilyId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<FamilyDomain.FamilyAddressDomain,Integer> familyAddressFamilyIdTargetSetLambda =FamilyDomain.FamilyAddressDomain::setFamilyId;

     /**
     * REF  source lambda
     */
     public static SFunction<FamilyDomain, Serializable> familyName_RelatedFamilyAddress_SourceLambda = FamilyDomain::getName;

     /**
     * REF  target lambda
     */
     public static SFunction<FamilyDomain.FamilyAddressDomain,Serializable> familyAddress_familyNameTargetLambda =FamilyDomain.FamilyAddressDomain::getFamilyName;

     /**
     * REF  target lambda
     */
     public static BiConsumer<FamilyDomain.FamilyAddressDomain,String> familyAddressFamilyNameTargetSetLambda =FamilyDomain.FamilyAddressDomain::setFamilyName;

    /**
    *  family_member lambda
    */
    public static SFunction<FamilyDomain.FamilyMemberDomain, Serializable> familyMemberDomainKeyLambda = FamilyDomain.FamilyMemberDomain::getId;


    /**
    * REF  source lambda
    */
    public static SFunction<FamilyDomain, Serializable> familyId_RelatedFamilyMember_SourceLambda = FamilyDomain::getId;

    /**
    * REF  target lambda
    */
    public static SFunction<FamilyDomain.FamilyMemberDomain,Serializable> familyMember_familyIdTargetLambda =FamilyDomain.FamilyMemberDomain::getFamilyId;

    /**
    * REF  target lambda
    */
    public static BiConsumer<FamilyDomain.FamilyMemberDomain,Integer> familyMemberFamilyIdTargetSetLambda =FamilyDomain.FamilyMemberDomain::setFamilyId;

     /**
     * REF  source lambda
     */
     public static SFunction<FamilyDomain, Serializable> familyName_RelatedFamilyMember_SourceLambda = FamilyDomain::getName;

     /**
     * REF  target lambda
     */
     public static SFunction<FamilyDomain.FamilyMemberDomain,Serializable> familyMember_familyNameTargetLambda =FamilyDomain.FamilyMemberDomain::getFamilyName;

     /**
     * REF  target lambda
     */
     public static BiConsumer<FamilyDomain.FamilyMemberDomain,String> familyMemberFamilyNameTargetSetLambda =FamilyDomain.FamilyMemberDomain::setFamilyName;
}
