package com.artframework.sample.controllers;

import com.artframework.domain.core.constants.Order;
import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/family/v1")
public class FamilyController {
    @Autowired
    private FamilyService familyService;

    /**
    * 查找
    * @param request 请求体
    * @return FamilyDomain
    */
    @PostMapping("/query")
    public FamilyDomain find(@RequestBody FamilyFindDomain request){
        return FamilyDomain.load(1, familyService)
                .loadRelated(FamilyDomain.FamilyAddressDomain.class)
                .loadRelated(FamilyDomain.FamilyMemberDomain.class,
                        x -> x.eq(FamilyDomain.FamilyMemberDomain::getType, "SON")
                                .or().eq(FamilyDomain.FamilyMemberDomain::getType, "DAUGHTER")
                                .orderBy(FamilyDomain.FamilyMemberDomain::getId, Order.DESC));
    }

    /**
    * 新增
    * @param request 请求体
    * @return Long
    */
    @PutMapping()
    public Long insert(@RequestBody FamilyDomain request){
        return familyService.insert(request);
    }

    /**
    * 修改
    * @param request 请求体
    * @return 成功OR失败
    */
    @PostMapping()
    public Boolean update(@RequestBody FamilyDomain request){
        return familyService.update(request);
    }

    /**
    * 删除
    * @param key 数据ID
    * @return 成功OR失败
    */
    @DeleteMapping
    public Boolean delete(@RequestParam("key") Long key){
        return familyService.delete(key);
    }
}
