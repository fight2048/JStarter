package com.fight2048.oss.autoconfigure.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.fight2048.oss.OssProperties;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
     * @param bucketName 存储桶名称
     * @return 域名地址
     */
    public String getEndpoint(String bucketName) {
        OssProperties.AliyunOssProperties properties = ossProperties.getAliyun();
        return String.format("%s://%s.%s", properties.getScheme(), bucketName, properties.getEndpoint());
    }

    /**
     * 获取 OSS 域名地址
     *
     * @return 域名地址
     */
    public String getEndpoint() {
        return getEndpoint(ossProperties.getAliyun().getBucketName());
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    public void createBucket(String bucketName) {
        if (!bucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
    }

    public List<Bucket> getAllBuckets() {
        return ossClient.listBuckets();
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

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    public void deleteBucket(String bucketName) {
        if (bucketExist(bucketName)) {
            ossClient.deleteBucket(bucketName);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExist(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    /**
     * 拷贝文件
     *
     * @param sourceBucketName      源存储桶名称
     * @param key                   存储桶文件名称
     * @param destinationBucketName 目标存储桶名称
     */
    public CopyObjectResult copyObject(String sourceBucketName, String key, String destinationBucketName) {
        return copyObject(sourceBucketName, key, destinationBucketName, key);
    }

    /**
     * 拷贝文件，重命名
     *
     * @param sourceBucketName      源存储桶名称
     * @param sourceKey             存储桶文件名称
     * @param destinationBucketName 目标存储桶名称
     * @param destinationKey        目标存储桶文件名称
     */
    public CopyObjectResult copyObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
        return ossClient.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶文件名称
     * @return 文件元信息
     */
    public ObjectMetadata getMetadata(String bucketName, String key) {
        return ossClient.getObjectMetadata(bucketName, key);
    }

    /**
     * 获取对象的url
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, expires);
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    /**
     * 获取文件相对路径
     *
     * @param key 存储桶对象名称
     * @return 文件相对路径
     */
    public String getObjectPath(String key) {
        return getBucketName().concat("/")
                .concat(key);
    }

    /**
     * 获取文件相对路径
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @return 文件相对路径
     */
    public String getObjectPath(String bucketName, String key) {
        return bucketName.concat("/")
                .concat(key);
    }


    /**
     * 获取文件地址
     *
     * @param key 存储桶对象名称
     * @return 文件地址
     */
    public String getObjectLink(String key) {
        return getEndpoint().concat("/").concat(key);
    }

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @return 文件地址
     */
    public String getObjectLink(String bucketName, String key) {
        return getEndpoint(bucketName).concat("/").concat(key);
    }

    /**
     * 上传文件
     *
     * @param file 上传文件类
     * @return 文件信息
     */
    public PutObjectResult upload(MultipartFile file) {
        return upload(file.getOriginalFilename(), file);
    }

    /**
     * 上传文件
     *
     * @param key  上传文件名
     * @param file 上传文件类
     * @return 文件信息
     */
    public PutObjectResult upload(String key, MultipartFile file) {
        return upload(ossProperties.getAliyun()
                .getBucketName(), key, file);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        上传文件名
     * @param file       上传文件类
     * @return 文件信息
     */
    @SneakyThrows
    public PutObjectResult upload(String bucketName, String key, MultipartFile file) {
        return upload(bucketName, key, file.getInputStream());
    }

    /**
     * 上传文件
     *
     * @param key    存储桶对象名称
     * @param stream 文件流
     * @return 文件信息
     */
    public PutObjectResult upload(String key, InputStream stream) {
        return upload(ossProperties.getAliyun().getBucketName(), key, stream);
    }

    /**
     * 删除文件
     *
     * @param key 存储桶对象名称
     */
    public void deleteObject(String key) {
        ossClient.deleteObject(getBucketName(), key);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     */
    public void deleteObject(String bucketName, String key) {
        ossClient.deleteObject(bucketName, key);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param keys       存储桶对象名称
     */
    public DeleteObjectsResult deleteObjects(String bucketName, List<String> keys) {
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
        request.setKeys(keys);
        return ossClient.deleteObjects(request);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    private PutObjectResult upload(String bucketName, String key, InputStream stream) {
        // 覆盖上传
        return ossClient.putObject(bucketName, key, stream);
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @return 上传凭证
     */
    public Map<String, Object> getUploadToken() {
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return getUploadToken(getBucketName(), dir);
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @param dir 上传后的目录
     * @return 上传凭证
     */
    public Map<String, Object> getUploadToken(String dir) {
        return getUploadToken(getBucketName(), dir);
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
        Duration expires = Duration.of(duration, timeUnit.toChronoUnit());
        Date expiration = Date.from(Instant.now().plus(expires));
        LocalDateTime localDateTime = LocalDateTime.ofInstant(expiration.toInstant(), ZoneId.systemDefault());

        OssProperties.AliyunOssProperties properties = ossProperties.getAliyun();

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
}
