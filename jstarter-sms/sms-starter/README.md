<h1 align="center"><a href="https://github.com/fight2048/jstarter" target="_blank">sms-starter</a></h1>

## 简介

`sms-starter` 主要是对一些云厂商的短信业务进行封装，支持`阿里云SMS、腾讯云SMS`。

## 使用

```xml
<dependency>
     <groupId>com.fight2048</groupId>
     <artifactId>sms-starter</artifactId>
     <version>0.0.1</version>
</dependency>
```

## 快速上手

sms-starter 提供了多种常见的短信业务支持。
1. 阿里云短信
2. 腾讯云短信

### 配置

#### 阿里云 SMS

##### 引入依赖

```xml
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-core</artifactId>
    <version>${aliyun.core.version}</version>
</dependency>

<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>dysmsapi20170525</artifactId>
    <version>${aliyun.sms.version}</version>
</dependency>
```

##### 配置文件

```yaml
sms:
  aliyun:
    accessKeyId: ***
    accessKeySecret: ***
    endpoint: dysmsapi.aliyuncs.com
    enabled: true
```
### 使用

#### 默认（推荐）

使用`SmsTemplate`作为注入对象，相当于策略模式，在配置文件中进行配置`enabled: true`即可表达使用的哪种短信。

```java
@Autowired
private SmsTemplate smsTemplate;
```

#### 阿里云 OSS

```java
@Autowired
private AliSmsTemplate aliSmsTemplate;
```

## 特点

- 提供了统一的操作接口，`SmsTemplate` 后续集成其他对象存储，只需要实现该接口即可