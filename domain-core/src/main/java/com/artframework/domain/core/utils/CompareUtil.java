package com.artframework.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 列表比较工具类
 * 用于比较两个列表的差异，返回新增、更新、删除的元素
 * 
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/4/14
 */
public class CompareUtil {

    /**
     * 比较两个列表的差异
     * 
     * @param oldList      旧列表
     * @param newList      新列表
     * @param keyExtractor 主键提取函数
     * @param <T>          元素类型
     * @return 比较结果，包含新增、更新、删除的元素列表
     */
    public static <T> CompareResult<T> compareList(List<T> oldList, List<T> newList,
            Function<? super T, Serializable> keyExtractor) {
        if (keyExtractor == null) {
            throw new IllegalArgumentException("主键提取函数不能为空");
        }

        // 处理空列表情况
        if (CollUtil.isEmpty(oldList) && CollUtil.isEmpty(newList)) {
            return new CompareResult<>(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }

        if (CollUtil.isEmpty(oldList)) {
            return new CompareResult<>(new ArrayList<>(newList), Collections.emptyList(), Collections.emptyList());
        }

        if (CollUtil.isEmpty(newList)) {
            return new CompareResult<>(Collections.emptyList(), Collections.emptyList(), new ArrayList<>(oldList));
        }

        // 构建映射表
        Map<Serializable, T> oldMap = buildKeyMap(oldList, keyExtractor);

        // 分离有主键和无主键的新数据
        List<T> newItemsWithoutKey = new ArrayList<>();
        List<T> newItemsWithKey = new ArrayList<>();

        for (T item : newList) {
            Serializable key = keyExtractor.apply(item);
            if (ObjectUtil.isNull(key)) {
                newItemsWithoutKey.add(item);
            } else {
                newItemsWithKey.add(item);
            }
        }

        Map<Serializable, T> newMap = buildKeyMap(newItemsWithKey, keyExtractor);

        // 计算差异
        Set<Serializable> oldKeys = oldMap.keySet();
        Set<Serializable> newKeys = newMap.keySet();

        // 删除的元素：在旧列表中存在，在新列表中不存在
        List<T> deleteItems = oldKeys.stream()
                .filter(key -> !newKeys.contains(key))
                .map(oldMap::get)
                .collect(Collectors.toList());

        // 新增的元素：无主键的元素 + 在新列表中存在但在旧列表中不存在的元素
        List<T> addItems = new ArrayList<>(newItemsWithoutKey);
        newKeys.stream()
                .filter(key -> !oldKeys.contains(key))
                .map(newMap::get)
                .forEach(addItems::add);

        // 更新的元素：在新旧列表中都存在的元素
        List<T> updateItems = newKeys.stream()
                .filter(oldKeys::contains)
                .map(newMap::get)
                .collect(Collectors.toList());

        return new CompareResult<>(addItems, updateItems, deleteItems);
    }

    /**
     * 构建主键到对象的映射表
     */
    private static <T> Map<Serializable, T> buildKeyMap(List<T> list, Function<? super T, Serializable> keyExtractor) {
        return list.stream()
                .filter(item -> ObjectUtil.isNotNull(keyExtractor.apply(item)))
                .collect(Collectors.toMap(
                        keyExtractor,
                        Function.identity(),
                        (existing, replacement) -> {
                            // 如果有重复键，保留后面的元素
                            return replacement;
                        },
                        LinkedHashMap::new // 保持插入顺序
                ));
    }

    /**
     * 比较结果封装类
     * 
     * @param <T> 元素类型
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class CompareResult<T> {
        /** 新增的元素列表 */
        private List<T> addList;

        /** 更新的元素列表 */
        private List<T> updateList;

        /** 删除的元素列表 */
        private List<T> deleteList;

        /**
         * 检查是否有任何变更
         */
        public boolean hasChanges() {
            return CollUtil.isNotEmpty(addList) ||
                    CollUtil.isNotEmpty(updateList) ||
                    CollUtil.isNotEmpty(deleteList);
        }

        /**
         * 获取变更统计信息
         */
        public String getChangesSummary() {
            return String.format("新增: %d, 更新: %d, 删除: %d",
                    CollUtil.size(addList),
                    CollUtil.size(updateList),
                    CollUtil.size(deleteList));
        }
    }
}