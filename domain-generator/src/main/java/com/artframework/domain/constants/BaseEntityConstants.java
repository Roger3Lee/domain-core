package com.artframework.domain.constants;

import cn.hutool.core.collection.ListUtil;

import java.util.List;
import java.util.Set;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/1/4
 **/
public class BaseEntityConstants {
    public static final List<String> FIELDS = ListUtil.toList("id",
            "create_time", "create_user_account", "create_user_name_pt", "create_user_name_hant"
            , "last_update_time", "last_update_user_account", "last_update_user_name_pt", "last_update_user_name_hant"
            , "soft_delete", "soft_delete_time", "soft_delete_user_account", "soft_delete_user_name_pt", "soft_delete_user_name_hant");

    public static final String BASE_ENTITY = "mo.gov.dsaj.parent.core.mybatis.dataobject.BaseEntityDO";
    public static final String DOMAIN_CORE_PACKAGE = "mo.gov.dsaj.domain.core";
    public static final String BASE_MAPPER="mo.gov.dsaj.parent.core.mybatis.CustomBaseMapper";
}
