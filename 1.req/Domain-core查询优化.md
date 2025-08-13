# 角色
你是一个精通领域模型设计的高级软件开发工程师

# 现状
`domain-generator`项目用来生成领域模型的相关代码 
- 其中`*FindDomain`的结构不适合前端进行相关过滤条件传递，现需要进行重构。

## 现结构
```json
{
  "query": {
    "FamilyMemberDomain": {
      "filter": {
        "op": "AND",
        "condition": [
          {
            "field": "name",
            "op": "=",
            "value": "王芳"
          },
          {
            "field": "name",
            "op": "LIKE",
            "value": "张三"
          }
        ]
      },
      "order": null
    },
    "FamilyAddressDomain": {
      "filter": {
        "op": "AND",
        "condition": []
      },
      "order": null
    }
  },
  "loadAll": null,
  "loadFamilyAddressDomain": true,
  "loadFamilyMemberDomain": true
}
```
## 期望优化后的结构
```json
{
  "query": {
    "FamilyMemberDomain": {
      "filter": {
        "condition": {
            "name": "王芳",
            "age": "18"
          },
          "op":{
            "age": "LIKE"
          }
      },
      "order": null
    },
    "FamilyAddressDomain": {
      "filter": {
        "op": "AND",
        "condition": []
      },
      "order": null
    }
  },
  "loadAll": null,
  "loadFamilyAddressDomain": true,
  "loadFamilyMemberDomain": true
}
```

# 要求：
***禁止***调整现有的```BaseLoadFlag```的结构。 
***调整***`*FindDomain`的代码生成规则， 生成优化后的结构。
***抽象***`*FindDomain`中的LoadFlag向领域`*Domain`的LoadFlag的映射 