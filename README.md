# 简介
## 功能
此框架提供基于DDD（领域驱动）脚手架功能，开发者可根据业务需要定义好领域模型来生成包含:实体（Entity）、聚合及聚合根（Aggregate，Aggregate Root）、仓储（Repository）、领域服务（Domain Service）在内的代码。
## DDD概念介绍

![img](https://pic1.zhimg.com/80/v2-40c06b92067ce29b1fdf5cc5404f6f48_720w.webp)

### 实体（Entity）
实体是唯一的且可持续变化的。意思是说在实体的生命周期内，无论其如何变化，其仍旧是同一个实体。唯一性由唯一的身份标识来决定的。可变性也正反映了实体本身的**状态**和**行为**。

### 聚合及聚合根（Aggregate，Aggregate Root）

聚合，它通过定义对象之间清晰的所属关系和边界来实现领域模型的内聚，并避免了错综复杂的难以维护的对象关系网的形成。聚合定义了一组具有内聚关系的相关对象的集合，我们把聚合看作是一个修改数据的单元。

### 领域服务（Domain Service）

领域模型中的对象自从被创建出来后不会一直留在内存中活动的，当它不活动时会被持久化到数据库中，然后当需要的时候我们会重建该对象；重建对象就是根据数据库中已存储的对象的状态重新创建对象的过程；所以，可见重建对象是一个和数据库打交道的过程。从更广义的角度来理解，我们经常会像集合一样从某个类似集合的地方根据某个条件获取一个或一些对象，往集合中添加对象或移除对象。也就是说，我们需要提供一种机制，可以提供类似集合的接口来帮助我们管理对象

### 仓储（Repository）

领域模型中的对象自从被创建出来后不会一直留在内存中活动的，当它不活动时会被持久化到数据库中，然后当需要的时候我们会重建该对象；重建对象就是根据数据库中已存储的对象的状态重新创建对象的过程；所以，可见重建对象是一个和数据库打交道的过程。从更广义的角度来理解，我们经常会像集合一样从某个类似集合的地方根据某个条件获取一个或一些对象，往集合中添加对象或移除对象。也就是说，我们需要提供一种机制，可以提供类似集合的接口来帮助我们管理对象

## 结构

### domain-dependencies

维护包括domain-core、hutool、mapstruct和mybatis plus的依赖管理

### domain-core

提供基础的dto、domain service 和 repository和一些依赖的公共帮助类。

### domain-generator

提供代码脚手架功能。基于表配置文件和领域配置文件，生成包括entity、domain service 、repository和mybatis plus mapper的代码。

### domain-sample

基于config/table-list.xml和config/domain-config.xml文件生成的代码提供的spring boot样例。

## 依赖
Mybatis plus, mapstruct、hutool

## 领域域配置文件和表配置文件

### 表配置文件

#### XML样例

### 领域域配置文件

### XML样例

```xml
<domains>
    <domain name="user" description="用戶域" main-table="user_info">
        <related table="user_address" many="false" fk="id:user_id"/>
        <related table="user_family_member" many="true" fk="id:user_id"/>
    </domain>
</domains>
```

### XML介绍
| 属性        | 类型   | 描述                                              |
| ----------- | ------ | ------------------------------------------------- |
| name        | STRING | 域名称                                            |
| description | STRING | 域描述                                            |
| main-table  | STRING | 模型的实体                                        |
| related     | STRING | 实体关联的值/引用对象集合                         |
| - table     | STRING | 值/引用对象的表名                                 |
| - many      | BOOL   | 是否为一对多，true为一对多， false为一对          |
| - fk        | STRING | 实体和值/引用对象的对应的表的关联字段。使用：隔开 |

# 样例

### 脚手架生成代码结构

- controller: 生成domain服务的增删改查和分页查询接口

- domain：生成包括领域服务（Domain Service）、仓储（Repository）在内的代码。同时通过实体或值/引用类型的状态进行数据的增删改查操作。

- entities：通过表配置文件生成的mybatis plus表实体类

- mappers: 通过表配置文件生成的mybatis plus mapper类


