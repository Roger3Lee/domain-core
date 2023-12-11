package com.artframework.domain.core.uitls;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/11
 * 用於通過實體過濾過濾條件
 *
 **/
public class LoadFiltersUtils {


    /**
     * 原始過濾條件
     *
     * @param filters
     * @param entityPrefix
     * @return
     */
    public static Map<String, Object> getEntityFilters(Map<String, Object> filters,String entityPrefix) {
        Map<String, Object> filterMap = new HashMap();
        if (ObjectUtil.isNotNull(filters)) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String prefix = entityPrefix + ".";
                if (entry.getKey().startsWith(prefix)) {
                    filterMap.put(entry.getKey().replace(prefix, ""), entry.getValue());
                }
            }
        }
        return filterMap;
    }
}
