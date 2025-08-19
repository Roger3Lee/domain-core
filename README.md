# 简介
## 功能
此框架提供基于DDD（领域驱动）脚手架功能，开发者可根据业务需要定义好领域模型来生成包含:实体（Entity）、聚合及聚合根（Aggregate，Aggregate Root）、仓储（Repository）、领域服务（Domain Service）在内的代码。

## 数据库支持

框架提供了针对不同数据库的专门支持模块，使用者可以根据项目需求选择相应的依赖包：

### MySQL 支持
- **模块**：`domain-mysql-support`
- **功能**：提供 MySQL 特有的批量操作，包括批量插入、ON DUPLICATE KEY UPDATE、REPLACE INTO、INSERT IGNORE 等
- **适用场景**：使用 MySQL 数据库的项目

### PolarDB 支持
- **模块**：`domain-polardb-support`
- **功能**：PolarDB-O 兼容 Oracle 语法，提供 INSERT ALL 批量插入、MERGE 批量更新等高级功能
- **适用场景**：使用阿里云 PolarDB-O（Oracle 兼容版）的项目

### PostgreSQL 支持
- **模块**：`domain-postgresql-support`
- **功能**：提供 PostgreSQL 特有功能，包括 ON CONFLICT、COPY 批量导入、RETURNING 子句、MERGE 操作等
- **适用场景**：使用 PostgreSQL 数据库的项目

### Oracle 支持
- **模块**：`domain-oracle-support`
- **功能**：提供 Oracle 原生的批量操作，包括 INSERT ALL、MERGE 语句等
- **适用场景**：使用 Oracle 数据库的项目

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

### 数据库支持模块

- **domain-mysql-support**：MySQL 数据库专用支持模块
- **domain-polardb-support**：PolarDB 数据库专用支持模块  
- **domain-postgresql-support**：PostgreSQL 数据库专用支持模块

## 依赖
Mybatis plus, mapstruct、hutool

## 快速开始

### 1. 选择数据库支持模块

根据您使用的数据库类型，选择相应的支持模块：

```xml
<!-- MySQL 支持 -->
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-mysql-support</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>

<!-- PolarDB 支持 -->
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-polardb-support</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>

<!-- PostgreSQL 支持 -->
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-postgresql-support</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>

<!-- Oracle 支持 -->
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-oracle-support</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```