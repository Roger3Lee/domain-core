---
name: ddd-development
description: 使用已生成的 Java DDD 领域模型代码实现业务功能，涵盖聚合 CRUD、FindDomain、LoadFlag、LambdaQuery、分页、事务和扩展边界。用户要求调用或扩展生成的 Domain、Service、Repository，处理关联数据或领域查询时使用。
---

# 基于领域模型进行代码开发

## 目标与优先级

本技能指导大模型**使用已有生成代码**完成业务开发，不指导手工重建领域模型或生成脚手架。先读取目标领域已有的 `*Domain`、`*FindDomain`、`*Service`、`*Repository`、`*LambdaExp` 和 Controller/AppService，按已生成的方法签名与模块惯例实现。

以 `领域驱动开发规范.md` 和目标模块的现有代码为准；若两者与本技能冲突，以前两者为准。模型或表结构确需变化时，修改 XML/DDL 并重新生成，而非手改生成层。

## 生成代码边界

以下通常是生成代码：DO、`*Domain`、`*FindDomain`、`*Service`、`*ServiceImpl`、`*Repository`、`*RepositoryImpl`、`*Mapper`、Convertor 和 LambdaExp。不得为业务需求直接修改这些文件；`*ConvertorDecorator` 是聚合转换的指定扩展点，按下一节规则处理。

业务代码放在生成器保护的 Controller、AppService/AppServiceImpl，或项目约定的独立扩展类中。自定义 SQL 放在独立的 `*CustomMapper`；先确认 `LambdaQuery` 无法表达需求，并在代码中说明原因。修改模型后按生成器覆盖策略重新生成。

## 使用 ConvertorDecorator 扩展聚合转换

当业务逻辑发生在聚合 `Domain → DO` 的持久化转换过程中，优先在 `{Name}ConvertorDecorator` 增加 MapStruct 转换钩子，而不是在 Controller、Mapper 或生成的 Convertor 中重复实现。

- 在 `@BeforeMapping` 方法中处理转换前的领域数据准备或校验。
- 在带有 `@MappingTarget` 的 `@AfterMapping` 方法中处理 `{Name}Domain → {Name}DO` 转换后的业务逻辑，例如补充持久化冗余字段、标准化字段值或根据聚合状态设置 DO 字段。
- 方法参数使用源 Domain 和目标 DO；将逻辑限定在本次转换需要的数据，避免在 Decorator 中执行查询、远程调用或开启事务。
- 聚合内关联对象也需要转换逻辑时，为对应关联 Domain 和关联 DO 增加匹配的 MapStruct 映射钩子，并确认生成的 Convertor 已通过 `@BeanMapping(qualifiedByName = { "{Name}ConvertorDecorator" })` 引用该 Decorator。
- Decorator 随 `--update-domain` 重新生成时可能被覆盖；修改领域模型并重新生成前，先保留并在生成后恢复这些业务钩子，或将稳定逻辑沉淀到生成模板/项目约定的可复用组件。

## 使用生成领域代码的流程

1. **定位聚合与入口**
   - 找到目标 `{Name}Domain`、`{Name}FindDomain`、`{Name}Service` 和对应 `LoadFlag`。
   - Controller 按模块既有模式调用 AppService 或生成的 DomainService；两者都不得直接访问 Repository 或 Mapper。
   - 让 DomainService 调用 Repository，由 Repository 调用 Mapper；不得跨层绕过生成的聚合 CRUD 能力。

2. **新增聚合**
   - 将主对象和需要同时创建的关联对象填入同一个 `{Name}Domain` 请求。
   - 调用生成的 `service.insert(request)`。关联集合或对象有值时，框架会处理外键与级联插入；新增关联数据不依赖 LoadFlag。
   - 需要多个聚合或外部资源协作时，在 AppService 编排，并用 `@Transactional(rollbackFor = Exception.class)` 包裹完整操作。

3. **查询聚合**
   - 按主键获取聚合时，优先使用 `{Name}Domain.load(id, service)`；按业务键获取时，优先使用 `{Name}Domain.loadByKey(key, {Name}Domain::getBusinessKey, service)`。它们会为聚合绑定 Service，供后续 `loadRelated` 使用。
   - 取得聚合后，按需调用 `domain.loadRelated(RelatedDomain.class)`；需过滤或排序关联对象时使用 `domain.loadRelated(RelatedDomain.class, query -> ...)`。不得在分页或列表循环中逐条调用该方法。
   - 仅在接口需要一次性声明关联数据加载范围或更新范围时，使用 `{Name}FindDomain.builder().key(id).loadFlag(loadFlag).build()` 后调用 `service.find(request)`；需要关联对象时显式设置 `loadAll` 或对应 `loadXxx`。

4. **更新与删除聚合**
   - 更新请求必须携带准确的 LoadFlag。只有 `loadAll` 或已标记 `loadXxx` 的关联对象会参与新增、更新和删除合并。
   - 更新前先以相同的 LoadFlag 查询原始聚合，再调用 `service.update(request)`，使框架能够完成差异比较和子对象合并。
   - 删除使用生成的 `service.delete(...)`；确认 LoadFlag 覆盖应级联删除的关联对象，并确认业务上允许该级联行为。

5. **列表、分页与批量处理**
   - 优先构造 `LambdaQuery.of(Domain.class)`，使用 `eq`、`in`、`like`、`between`、`and`、`or` 和 `orderBy` 表达条件。
   - 列表和分页优先调用生成 Service/Repository 的 `queryList`、`queryPage` 等方法，并使用 `PageDomain` 传递页码和页大小。
   - 批量写入、更新或删除应使用框架提供的批量能力，分批处理超大数据集，并始终置于事务内。

## LoadFlag 是读写范围契约

LoadFlag 不仅决定 `find` 时加载哪些关联对象，也决定 `update` 时哪些关联对象可被持久化。不要把它当成可选的展示参数：

- 只更新主表：不标记关联对象。
- 更新某个关联集合：查询和更新都标记同一个 `loadXxx`。
- 更新全部关联数据：使用 `loadAll`，并确保请求包含完整、可信的关联集合，避免误删未携带的数据。
- 分页列表：不要对每条记录调用 `loadRelated()`；应使用批量加载或重新设计查询范围以避免 N+1。

## 前端 changed 状态跟踪

领域模型返回前端后，使用变更跟踪工具维护其 `changed` 标签，以便后端增量更新能识别发生修改的聚合和关联对象。

- API 成功响应进入前端状态前，调用 `attachSnapshot(data)`：对响应数据深拷贝，并以不可枚举 `Symbol` 属性保存干净基线；该属性不会进入 JSON、展开运算或请求体。
- React 组件使用 `useAutoMarkChanged(initialData)` 替代直接 `useState` 管理可编辑的领域模型。每次通过 setter 更新状态时，Hook 对比快照并递归将实际变化、且自身拥有 `changed` 字段的对象标记为 `changed = true`。
- 对嵌套关联对象和数组，必须使用不可变更新并通过 Hook 返回的 setter 提交完整新状态；不要直接修改对象，以确保变更监听生效。
- 重新从服务端加载或保存成功后，调用 Hook 返回的 `resetSnapshot(cleanData)` 刷新基线，避免旧快照持续将已保存数据标记为变更。
- 提交前调用 `stripInternalFields(data)` 移除前端 `_tempId`；不要提交 Symbol 快照或其他前端内部字段。该工具仅移除传入对象或数组元素的顶层 `_tempId`，嵌套临时字段需按数据结构另行清理。
- 仅在数据相对服务端基线发生真实变化时设置 `changed`；比较时忽略 `changed` 自身，避免标记动作触发无限状态更新。

实现与集成该功能时，先阅读 [changeTracking.ts](references/changeTracking.ts) 参考实现；将其复制到目标 React 前端项目后，再按项目的 API 拦截器与领域模型类型调整。

## 查询限制与事务规则

领域模型适用于单个聚合根的 CRUD、级联操作、增量更新和可由 LambdaQuery 表达的单表查询。**多表关联查询，尤其是跨聚合根的 JOIN，不适合使用领域模型或通过 `loadRelated` 拼装。**

多表 JOIN、GROUP BY/聚合函数、复杂子查询、窗口函数、CTE 或无法拆为独立查询后在内存合并的跨聚合查询，应使用 MyBatis 自定义 SQL：

- 在生成 Mapper 目录之外创建 `mapper/{Name}CustomMapper.java`，返回专用 DTO/VO，而不是聚合 Domain。
- 在 AppService 层调用 CustomMapper；调用前在代码注释中说明 DomainService 与 LambdaQuery 均无法满足的原因。
- 只读查询不需要为查询本身创建事务；自定义写 SQL、跨 Repository 的写操作和多步写操作必须使用 `@Transactional(rollbackFor = Exception.class)`。

对于单聚合、单表的筛选、排序、分页和批量 ID 加载，仍优先使用生成的 DomainService 与 LambdaQuery。不要假定单个生成 API 能自动覆盖自定义流程的原子性。

## 交付说明模板

完成实现后，简要说明：

```text
- 聚合：<使用的 Domain / FindDomain / Service>
- 操作：<insert / find / update / delete / query>
- 加载范围：<LoadFlag 及关联对象范围>
- 扩展：<AppService 或 CustomMapper；若无则写“无”>
- 验证：<已执行的编译、测试和关联数据检查>
```

## 快速检查清单

- [ ] 已读取并复用目标领域生成的 API 与类型
- [ ] 未修改生成的 Domain、Service、Repository、Mapper 或 Convertor
- [ ] Domain 到 DO 的转换业务逻辑位于 ConvertorDecorator 的映射钩子
- [ ] 未从 Controller/AppService 直接访问 Repository 或 Mapper
- [ ] update 的 LoadFlag 与查询、关联更新范围一致
- [ ] 前端编辑通过快照和 `useAutoMarkChanged` 正确维护 changed 标签
- [ ] 提交前已移除 `_tempId` 等前端内部字段
- [ ] 列表/分页不会通过逐条关联加载产生 N+1
- [ ] 跨聚合多表 JOIN 使用 CustomMapper 与专用 DTO/VO，不强行组装 Domain
- [ ] 多步写操作和自定义写 SQL 位于事务内
- [ ] 编译和相关测试已通过
