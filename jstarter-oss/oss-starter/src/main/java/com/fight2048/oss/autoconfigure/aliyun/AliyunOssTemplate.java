package com.fight2048.oss.autoconfigure.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.fight2048.oss.OssProperties;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2020-03-07 0007 下午 10:46
 * @version: v0.0.0
 * @description: 阿里云 OSS 操作，参考文档：https://help.aliyun.com/zh/oss/developer-reference/description
 */
public class AliyunOssTemplate {
    private final OSS ossClient;
    private final OssProperties ossProperties;

    public AliyunOssTemplate(OSS ossClient, OssProperties ossProperties) {
        this.ossClient = ossClient;
        this.ossProperties = ossProperties;
    }

    /**
     * 获取 OSS 域名地址
     *
     * @return 域名地址
     */
    public String getEndpoint() {
        return getEndpoint(ossProperties.getAliyun()
                .getBucketName());
    }

    /**
     * 获取 OSS 域名地址
     *
     * @param bucketName 存储桶名称
     * @return 域名地址
     */
    public String getEndpoint(String bucketName) {
        OssProperties.AliyunOssProperties properties = ossProperties.getAliyun();
        return String.format("%s://%s.%s", properties.getScheme(), bucketName, properties.getEndpoint());
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @param dir        上传后的目录
     * @return 上传凭证
     */
    public Map<String, Object> getUploadToken(String bucketName, String dir) {
        // 默认过期时间1天，单位秒
        OssProperties.AliyunOssProperties properties = ossProperties.getAliyun();
        long expireTime = 24 * 60 * 60;
        expireTime = Optional.ofNullable(properties)
                .map(p -> p.getExtension())
                .map(kv -> Long.valueOf(kv.get("expireTime", 24 * 60 * 60)))
                .orElse(expireTime);

        return getUploadToken(bucketName, dir, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @param dir        上传后的目录
     * @param duration   过期间隔时间，单位配合timeUnit
     * @param timeUnit   过期单位
     * @return 上传凭证
     */
    public Map<String, Object> getUploadToken(String bucketName, String dir, long duration, TimeUnit timeUnit) {
        OssProperties.AliyunOssProperties properties = ossProperties.getAliyun();

        long expire = System.currentTimeMillis() + timeUnit.toMillis(duration);
        Date expiration = new Date(expire);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());

        PolicyConditions policy = new PolicyConditions();
        //默认大小限制1G
        long contentLengthRange = 1024 * 1024 * 1024;
        contentLengthRange = Optional.ofNullable(properties)
                .map(p -> p.getExtension())
                .map(kv -> Long.valueOf(kv.get("contentLengthRange", 1024 * 1024 * 1024)))
                .orElse(contentLengthRange);

        policy.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, contentLengthRange);
        policy.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policy);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String signature = ossClient.calculatePostSignature(postPolicy);

        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("accessId", properties.getAccessKey());
        map.put("policy", encodedPolicy);
        map.put("signature", signature);
        map.put("dir", dir);
        map.put("host", getEndpoint(bucketName));
        map.put("expire", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return map;
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    public String getBucketName() {
        return Optional.ofNullable(ossProperties.getAliyun())
                .map(p -> p.getBucketName())
                .orElse("");
    }
}
