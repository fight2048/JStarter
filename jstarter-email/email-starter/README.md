<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/tree/master/magic-starter-oss" target="_blank">oss-starter</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-starter-oss" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-starter-oss.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`oss-starter` 主要是对一些常用的对象存储的封装，支持`阿里云OSS、腾讯云COS、七牛云存储、MinIO`。

## 使用

```xml
<dependency>
     <groupId>com.fight2048</groupId>
     <artifactId>oss-starter</artifactId>
     <version>0.0.1</version>
</dependency>
```

## 快速上手

oss-starter 提供了 4 种常见的对象云存储的支持。
1. 阿里云 OSS
2. 腾讯云 COS
3. 七牛云存储
4. MinIO 自建


### 配置

#### 阿里云 OSS

##### 引入依赖

```xml
<dependency>
  <groupId>com.aliyun.oss</groupId>
  <artifactId>aliyun-sdk-oss</artifactId>
  <version>${aliyun.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
oss:
  ali-oss:
    enabled: true
    access-key: **************
    secret-key: *************
    endpoint: oss-com-hangzhou.aliyuncs.com
    bucket-name: test
    https: true
```

#### 腾讯云 COS

##### 引入依赖

```xml
<dependency>
  <groupId>com.qcloud</groupId>
  <artifactId>cos_api</artifactId>
  <version>${qcloud.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    tencent-cos:
      enabled: true
      app-id: ****
      access-key: ******************
      secret-key: **************
      bucket-name: test
      region: ap-shanghai
      https: true
```

#### 七牛云

##### 引入依赖

```xml
<dependency>
  <groupId>com.qiniu</groupId>
  <artifactId>qiniu-java-sdk</artifactId>
  <version>${qiniu.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    qiniu-cloud:
      enabled: true
      access-key: ***********
      secret-key: *****************
      bucket-name: test
      endpoint: http://*****.bkt.clouddn.com
      region: z0
```

#### MinIO

##### 引入依赖

```xml
<dependency>
  <groupId>io.minio</groupId>
  <artifactId>minio</artifactId>
  <version>${minio.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    min-io:
      enabled: true
      access-key: minioadmin
      secret-key: minioadmin
      bucket-name: test
      endpoint: http://192.168.31.8:9000
```

### 使用

#### 默认（推荐）

使用`DefaultOssTemplate`作为注入对象，相当于策略模式，在配置文件中进行配置`enabled: true`即可表达使用的哪种OSS。

```java
@Autowired
private DefaultOssTemplate defaultOssTemplate;
```

#### 阿里云 OSS

```java
@Autowired
private AliOssTemplate aliOssTemplate;
```

#### 腾讯云 COS

```java
@Autowired
private TencentCosTemplate tencentCosTemplate;
```

#### 七牛云

```java
@Autowired
private QiNiuCloudTemplate qiNiuCloudTemplate;
```

#### MinIO

```java
@Autowired
private MinIoTemplate minIoTemplate;
```

## 特点

- 可以自定义bucketName及文件名
- 提供了统一的操作接口，`OssTemplate` 后续集成其他对象存储，只需要实现该接口即可