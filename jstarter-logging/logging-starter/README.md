<h1 align="center"><a href="https://github.com/fight2048/jstarter" target="_blank">sms-starter</a></h1>

## 简介

`logging-starter` 对SpringMVC的请求过程中产生的日志进行封装，并通过事件传递出来。

## 使用

```xml
<dependency>
     <groupId>com.fight2048</groupId>
     <artifactId>logging-starter</artifactId>
     <version>0.0.1</version>
</dependency>
```

## 快速上手

logging-starter 提供了多种多种注解和事件供选择使用。
1. Swagger2注解自动解析
2. Swagger3注解自动解析
3. 自定义注解自动解析

### 配置

##### 引入依赖

```xml
<dependency>
    <groupId>com.fight2048</groupId>
    <artifactId>sms-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

### 使用

#### 默认（推荐）

依靠Spring的事件机制，监听日志的产生，对应的日志处理有使用者决定。

```java

```

## 特点

- 提供了简单的产生日志的封装，通过事件传递事件，完全解耦。