package com.artframework.sample.domains.user.service;

import com.artframework.sample.domains.user.domain.*;

import com.artframework.domain.core.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

public interface UserService extends BaseDomainService {

    /**
    * 分页查询
    * @param request 请求体
    * @return
    */
    IPage<UserDomain> page(UserPageDomain request);

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    UserDomain find(UserFindDomain request);

    /**
    * 查找
    * @param request 请求体
    * @param domain 源domain,如果此參數不為空則直接使用此參數作為主實體
    * @return
    */
    UserDomain find(UserFindDomain request, UserDomain domain);


   /**
     * 通過已有實體查找
     * @param response 已有實體
     * @param loadFlag 加載參數
     * @return
     */
    UserDomain find(UserDomain response, UserDomain.LoadFlag loadFlag);


    /**
     * 查找
     * @param request 请求体
     * @return
     */
    UserDomain findByKey(UserFindDomain request, SFunction<UserDomain, Serializable> keyLambda);

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    java.lang.Long insert(UserDomain request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(UserDomain request);
    /**
    * 修改,此方法不用再加載domain主entity數據
    * @param request 请求体
    * @param domain 原始domain
    * @return 成功OR失败
    */
    Boolean update(UserDomain request, UserDomain domain);

   /**
    * 修改,此方法不用再加載domain主entity數據， reload參數True將重新加載數據， False將直接對request和domain進行比較， 適用於已將模型數據加載的情況
    * @param request 请求体
    * @param domain 原始domain
    * @param reload 是否使用request的loadFlag重新加載數據
    * @return 成功OR失败
    */
    Boolean update(UserDomain request, UserDomain domain, Boolean reload);
    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(java.lang.Long key);
}
