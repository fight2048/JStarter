package com.fight2048.oss.autoconfigure.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fight2048.oss.OssProperties;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2020-03-07 0007 下午 10:46
 * @version: v0.0.0
 * @description: 亚马逊云S3 OSS 操作，参考文档：https://docs.aws.amazon.com/s3/
 */
public class AwsOssTemplate {
    private final AmazonS3 amazonS3;
    private final OssProperties ossProperties;

    public AwsOssTemplate(AmazonS3 amazonS3, OssProperties ossProperties) {
        this.amazonS3 = amazonS3;
        this.ossProperties = ossProperties;
    }

    /**
     * 获取 OSS 域名地址
     *
     * @return 域名地址
     */
    public String getEndpoint() {
        return ossProperties.getAws().getEndpoint();
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    public void createBucket(String bucketName) {
        if (!bucketExist(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    /**
     * 获取全部bucket
     * <p>
     *
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS API
     * Documentation</a>
     */
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    public String getBucketName() {
        return Optional.ofNullable(ossProperties.getAws())
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
            amazonS3.deleteBucket(bucketName);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExist(String bucketName) {
        return amazonS3.doesBucketExistV2(bucketName);
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
        return amazonS3.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶文件名称
     * @return 文件元信息
     */
    public ObjectMetadata getMetadata(String bucketName, String key) {
        return amazonS3.getObjectMetadata(bucketName, key);
    }

    /**
     * 获取文件外链，只用于下载
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param seconds    过期时间，单位秒,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getObjectUrl(String bucketName, String key, int seconds) {
        return getObjectUrl(bucketName, key, Duration.ofSeconds(seconds));
    }

    /**
     * 获取文件外链，只用于下载
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param expires    过期时间,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getObjectUrl(String bucketName, String key, Duration expires) {
        return getSignedUrl(bucketName, key, expires, HttpMethod.GET);
    }

    /**
     * 获取文件上传外链，只用于上传
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param seconds    过期时间，单位秒,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getUploadUrl(String bucketName, String key, long seconds) {
        return getUploadUrl(bucketName, key, Duration.ofSeconds(seconds));
    }

    /**
     * 获取文件上传外链，只用于上传
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param expires    过期时间,请注意该值必须小于7天
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
     */
    public String getUploadUrl(String bucketName, String key, Duration expires) {
        return getSignedUrl(bucketName, key, expires, HttpMethod.PUT);
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param seconds    过期时间，单位秒,请注意该值必须小于7天
     * @param method     文件操作方法：GET（下载）、PUT（上传）
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration,
     * HttpMethod method)
     */
    public String getSignedUrl(String bucketName, String key, long seconds, HttpMethod method) {
        return getSignedUrl(bucketName, key, Duration.ofSeconds(seconds), method);
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param expires    过期时间，请注意该值必须小于7天
     * @param method     文件操作方法：GET（下载）、PUT（上传）
     * @return url
     * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration,
     * HttpMethod method)
     */
    public String getSignedUrl(String bucketName, String key, Duration expires, HttpMethod method) {
        // Set the pre-signed URL to expire after `expires`.
        Date expiration = Date.from(Instant.now().plus(expires));
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key).withMethod(method).withExpiration(expiration);
        // Generate the pre-signed URL.
        URL url = amazonS3.generatePresignedUrl(request);
        return url.toString();
    }

    /**
     * 获取文件URL
     * <p>
     * If the object identified by the given bucket and key has public read permissions
     * (ex: {@link CannedAccessControlList#PublicRead}), then this URL can be directly
     * accessed to retrieve the object's data.
     *
     * @param key 文件名称
     * @return url
     */
    public String getUrl(String key) {
        URL url = amazonS3.getUrl(getBucketName(), key);
        return url.toString();
    }

    /**
     * 获取文件URL
     * <p>
     * If the object identified by the given bucket and key has public read permissions
     * (ex: {@link CannedAccessControlList#PublicRead}), then this URL can be directly
     * accessed to retrieve the object's data.
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @return url
     */
    public String getUrl(String bucketName, String key) {
        URL url = amazonS3.getUrl(bucketName, key);
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
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @return 二进制流
     * @see <a href= "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    public S3Object getObject(String bucketName, String key) {
        return amazonS3.getObject(bucketName, key);
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
        return upload(getBucketName(), key, file);
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
        return upload(bucketName, key, file.getInputStream(), file.getSize(), file.getContentType());
    }

    /**
     * 上传文件
     *
     * @param key    存储桶对象名称
     * @param stream 文件流
     * @return 文件信息
     */
    public PutObjectResult upload(String key, InputStream stream) {
        return upload(getBucketName(), key, stream, null);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    private PutObjectResult upload(String bucketName, String key, InputStream stream, ObjectMetadata matadata) {
        return amazonS3.putObject(bucketName, key, stream, matadata);
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param key        文件名称
     * @param stream     文件流
     * @throws IOException IOException
     */
    public void upload(String bucketName, String key, InputStream stream) throws IOException {
        upload(bucketName, key, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 上传文件 指定 contextType
     *
     * @param bucketName  bucket名称
     * @param key         文件名称
     * @param stream      文件流
     * @param contextType 文件类型
     * @throws IOException IOException
     */
    public void upload(String bucketName, String key, String contextType, InputStream stream)
            throws IOException {
        upload(bucketName, key, stream, stream.available(), contextType);
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param key         文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @see <a href= "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
     * API Documentation</a>
     */
    public PutObjectResult upload(String bucketName, String key,
                                  InputStream stream, long size, String contextType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);
        metadata.setContentType(contextType);
        PutObjectRequest request = new PutObjectRequest(bucketName, key, stream, metadata);
        // Setting the read limit value to one byte greater than the size of stream will
        // reliably avoid a ResetException
        request.getRequestClientOptions()
                .setReadLimit(Long.valueOf(size).intValue() + 1);
        return amazonS3.putObject(request);
    }

    /**
     * 删除文件
     *
     * @param key 存储桶对象名称
     */
    public void deleteObject(String key) {
        amazonS3.deleteObject(getBucketName(), key);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     */
    public void deleteObject(String bucketName, String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param keys       存储桶对象名称
     */
    public DeleteObjectsResult deleteObjects(String bucketName, String... keys) {
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(keys);
        return amazonS3.deleteObjects(request);
    }
}
