# 角色
你是一个精通领域模型设计的高级软件开发工程师

# 现状
`domain-generator`项目用来生成领域模型的相关代码 
- 其中`*FindDomain`的结构不适合前端进行相关过滤条件传递，现需要优化一下结构

## 现结构
```json
{
  "changed": false,
  "id": 2,
  "name": "张三的家",
  "personCount": null,
  "familyAddress": {
    "changed": false,
    "id": 3,
    "familyId": 2,
    "familyName": "张三的家",
    "addressName": "武汉"
  },
  "familyMemberList": [
    {
      "changed": false,
      "id": 3,
      "familyId": 2,
      "familyName": "张三的家",
      "name": "张三",
      "phone": "180000000",
      "type": "P"
    }
  ],
  "loadFlag": {
    "filters": {
      "FamilyMemberDomain": {
        "op": "AND",
        "condition": [
          {
            "field": "name",
            "op": "=",
            "value": "王芳"
          }
        ]
      }
    },
    "orders": {"field": "name",},
    "loadAll": null,
    "loadFamilyAddressDomain": true,
    "loadFamilyMemberDomain": true
  }
}
```
## 期望优化后的结构
```json
{
  "changed": false,
  "id": 2,
  "name": "张三的家",
  "personCount": null,
  "familyAddress": {
    "changed": false,
    "id": 3,
    "familyId": 2,
    "familyName": "张三的家",
    "addressName": "武汉"
  },
  "familyMemberList": [
    {
      "changed": false,
      "id": 3,
      "familyId": 2,
      "familyName": "张三的家",
      "name": "张三",
      "phone": "180000000",
      "type": "P"
    }
  ],
  "loadFlag": {
    "condition": {
      "FamilyMemberDomain": {
        "filter": {"name": "王芳"},
        "order": {"name": "desc"}
      }
    },
    "orders": {},
    "loadAll": null,
    "loadFamilyAddressDomain": true,
    "loadFamilyMemberDomain": true
  }
}
```