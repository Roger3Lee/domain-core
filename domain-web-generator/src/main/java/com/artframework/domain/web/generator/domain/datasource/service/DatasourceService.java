package com.artframework.domain.web.generator.domain.datasource.service;

import com.artframework.domain.web.generator.domain.datasource.domain.*;

import com.artframework.domain.core.service.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

public interface DatasourceService extends BaseDomainService {

    /**
    * 查找
    * @param request 请求体
    * @return
    */
    DatasourceDomain find(DatasourceFindDomain request);

   /**
     * 通過已有實體查找
     * @param response 已有實體
     * @param loadFlag 加載參數
     * @return
     */
    DatasourceDomain find(DatasourceDomain response, DatasourceDomain.LoadFlag loadFlag);
    /**
     * 查找
     * @param request 请求体
     * @return
     */
    DatasourceDomain findByKey(DatasourceFindDomain request, SFunction<DatasourceDomain, Serializable> keyLambda);

    /**
    * 新增
    * @param request 请求体
    * @return
    */
    Integer insert(DatasourceDomain request);

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    Boolean update(DatasourceDomain request);
    /**
    * 修改,此方法不用再加載domain主entity數據
    * @param request 请求体
    * @param domain 原始domain
    * @return 成功OR失败
    */
    Boolean update(DatasourceDomain request, DatasourceDomain domain);
    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    Boolean delete(Integer key);
    /**
    * 删除
    * @param key 数据ID
    * @param loadFlag 數據加載參數
    * @return 成功OR失败
    */
    Boolean delete(Integer key, DatasourceDomain.LoadFlag loadFlag);
}
