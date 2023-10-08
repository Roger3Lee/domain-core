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

``` XML
<tables>
    <table name="base" basic="true">
        <column name="CREATED_TIME" type="java.util.Date" comment="創建時間"/>
        <column name="CREATED_BY" type="java.lang.String" comment="創建用戶"/>
        <column name="UPDATED_TIME" type="java.util.Date" comment="創建時間"/>
        <column name="UPDATED_BY" type="java.lang.String" comment="創建用戶"/>
    </table>
    <table name="user_info" inherit="base">
        <column name="id" type="java.lang.Long" comment="主鍵" key="true" keyGenerator="true"/>
        <column name="name" type="java.lang.String" comment="名字"/>
        <column name="phone" type="java.lang.String" comment="手机"/>
        <column name="family_member_count" type="java.lang.Integer" comment="家庭成員總數"/>
    </table>
</tables>
```
#### XML介绍

| 属性           | 类型   | 描述                                                         |
| -------------- | ------ | ------------------------------------------------------------ |
| name           | STRING | 表名称                                                       |
| basic          | BOOL   | 是否为基础表，可以将公用字段使用基础表进行定义               |
| inherit        | STRING | 继承的基础表                                                 |
| column         | LIST   | 表的列集合                                                   |
| - name         | STRING | 列字段名                                                     |
| - type         | STRING | 列字段对应的java类型                                         |
| - comment      | STRING | 列描述                                                       |
| - key          | BOOL   | 是否为主键                                                   |
| - keyGenerator | BOOL   | 是否为自增主键， 如果不是自增主键则会在实体类上增加@KeySequence注解，并使用seq_{table_name}_id生成主键seq. |



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

### POSTMAN 调用接口样例

- ​	新增用户，信息包括家庭住址和家庭成员，接口返回为数据新增后的主键

  ![image-20231008160429968](https://github.com/Roger3Lee/artframework.domain/blob/master/images/add.png)
  
- ​	分页查询

  ![image-20231008162534040](https://github.com/Roger3Lee/artframework.domain/blob/master/images/page-query.png)

- ​	领域实体查询

	可以通过loadFlag中关联的实体的加载属性来控制查询的数据，例如如下图：查询了包括家庭住址和家庭成员在内的数据

  ![image-20231008162751086](https://github.com/Roger3Lee/artframework.domain/blob/master/images/id-query1.png)

	如下图：则只查询了家庭成员的数据
![image-20231008163434202](https://github.com/Roger3Lee/artframework.domain/blob/master/images/id-query2.png)


- ​    修改

  修改家庭成员儿子的名字为"李伟"并增加女儿"李芳"。

  ![image-20231008164742955](https://github.com/Roger3Lee/artframework.domain/blob/master/images/update.png)再次查询家庭成员，发现实体聚合根属性familyMemberCount的值变成了2， 家庭成员儿子的姓名变成了李伟， 增加了女儿李芳。
  
  ![image-20231008165624529](https://github.com/Roger3Lee/artframework.domain/blob/master/images/id-query3.png)


- ​	删除

  删除成功后，实体和所关联的值/引用类型都将从数据库删除

  ![image-20231008165820553](C:\Users\浩鲸新智能\AppData\Roaming\Typora\typora-user-images\image-20231008165820553.png)


# VNEXT
- IDEA插件
- 基于数据库表生成实体（Entity)