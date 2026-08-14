package com.artframework.sample.controllers;

import com.artframework.sample.domains.family.domain.*;
import com.artframework.sample.domains.family.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/family/v1")
public class FamilyController {
    @Autowired
    private FamilyService familyService;

    /**
     * 根据主键查询领域对象，返回包含主表及关联表数据的完整DTO
     * <p>
     * loadFlag 控制查询时加载关联数据的范围：
     * <ul>
     *   <li>loadAll=true：加载所有关联表数据</li>
     *   <li>loadXxx=true：仅加载指定关联表数据（每个关联表对应一个 loadXxx 属性）</li>
     *   <li>不传或全部为false：仅加载主表数据</li>
     * </ul>
     * 此外，loadFlag 同时控制 update 接口的更新范围：只有 loadAll=true 或 loadXxx=true 的关联表，
     * 才会在后续调用 update 时被持久化（新增/修改/删除）；未标记的关联表数据将被忽略不做任何变更。
     *
     * @param key      主键标识
     * @param loadFlag 关联数据加载与更新范围标识，可选参数；不传时仅查询主表
     * @return FamilyDomain 领域数据传输对象
     */
    @PostMapping("/{key}")
    public FamilyDomain find(
            @PathVariable("key") Long key,
            @RequestBody(required = false) FamilyDomain.LoadFlag loadFlag
    ){
        loadFlag.lambdaQuery(FamilyDomain.FamilyAddressDomain.class, LambdaQuery.of(FamilyDomain.FamilyAddressDomain.class)
                .eq(FamilyDomain.FamilyAddressDomain::getAddressName,"武"));
        FamilyFindDomain request = FamilyFindDomain.builder()
                .key(key)
                .loadFlag(loadFlag != null ? loadFlag : new FamilyDomain.LoadFlag())
                .build();
        return familyService.find(request);
    }

    /**
     * 新增领域对象，将主表及 request 中携带的关联表数据一并持久化到数据库并返回生成的主键
     * <p>
     * 关联表数据是否插入取决于 request 中对应关联字段是否有值，与 loadFlag 无关。
     *
     * @param request 新增请求体，包含主表数据及需要新增的关联表数据
     * @return Long 新增记录的主键标识
     */
    @PutMapping()
    public Long insert(@RequestBody FamilyDomain request){
        return familyService.insert(request);
    }

    /**
     * 修改领域对象，根据 DTO 中的主键更新主表及指定范围的关联表数据
     * <p>
     * 关联表的更新范围由 request.loadFlag 控制：
     * <ul>
     *   <li>loadAll=true：合并（新增/更新/删除）所有关联表数据</li>
     *   <li>loadXxx=true：仅合并指定关联表数据</li>
     *   <li>未标记的关联表即使 request 中携带了数据也不会被更新</li>
     * </ul>
     * 因此，调用 update 前应先通过 find 接口并传入相同的 loadFlag 加载原始数据，
     * 以便框架对比新旧数据后正确执行差量更新。
     *
     * @param request 修改请求体，包含主表数据及需要更新的关联表数据（由 loadFlag 控制范围）
     * @return Boolean 更新操作是否成功
     */
    @PostMapping()
    public Boolean update(@RequestBody FamilyDomain request){
        return familyService.update(request);
    }

    /**
     * 根据主键删除领域对象，级联删除关联表数据
     *
     * @param key 待删除记录的主键标识
     * @return Boolean 删除操作是否成功
     */
    @DeleteMapping("/{key}")
    public Boolean delete(@PathVariable("key") Long key){
        return familyService.delete(key);
    }
}
