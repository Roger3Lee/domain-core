package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/4/14
 **/
public class CompareUtil {

    /**
     * 比較兩個列表是否相同
     * @param oldList
     * @param newList
     * @param <T>
     * @return Tuple get(0)為新增， get(1) 為刪除, get(2)為更新
     */
    public static <T> CompareResult<T> compareList(List<T> oldList, List<T> newList, Function<? super T, Serializable> keyWrap) {
        List<T> deleteItems = new ArrayList<>();
        List<T> addItems = new ArrayList<>();
        List<T> updateItems = new ArrayList<>();
        Map<Serializable, T> oldMap = new HashMap<>();
        Map<Serializable, T> newMap = new HashMap<>();
        if (CollUtil.isNotEmpty(oldList)) {
            oldMap = oldList.stream().collect(Collectors.toMap(keyWrap, x -> x, (prev, next) -> next));
        }
        if (CollUtil.isNotEmpty(newList)) {
            //将主键为空的数据增加到新增列表
            addItems.addAll(newList.stream().filter(x-> ObjectUtil.isNull(keyWrap.apply(x))).collect(Collectors.toList()));
            newMap = newList.stream().filter(x-> !ObjectUtil.isNull(keyWrap.apply(x))).collect(Collectors.toMap(keyWrap, x -> x, (prev, next) -> next));
        }

        List<Serializable> oldKeys = new ArrayList<>(oldMap.keySet());
        List<Serializable> newKeys = new ArrayList<>(newMap.keySet());
        Collection<Serializable> deletes = CollUtil.subtract(oldKeys, newKeys);
        Collection<Serializable> adds = CollUtil.subtract(newKeys, oldKeys);
        Collection<Serializable> updates = CollUtil.intersection(newKeys, oldKeys);

        Map<Serializable, T> finalOldMap = oldMap;
        deletes.forEach(x -> {
            deleteItems.add(finalOldMap.get(x));
        });

        Map<Serializable, T> finalNewMap = newMap;
        adds.forEach(x -> {
            addItems.add(finalNewMap.get(x)) ;
        });
        updates.forEach(x -> {
            updateItems.add(finalNewMap.get(x)) ;
        });

        return new CompareResult<T>(addItems,updateItems, deleteItems);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class CompareResult<T>{
        private List<T> addList;
        private List<T> updateList;
        private List<T> deleteList;
    }
}