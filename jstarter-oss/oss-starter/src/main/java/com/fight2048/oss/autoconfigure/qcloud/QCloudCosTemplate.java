package com.fight2048.oss.autoconfigure.qcloud;

import com.fight2048.oss.OssProperties;
import com.qcloud.cos.COS;
import com.qcloud.cos.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云 COS 操作，参考文档：https://cloud.tencent.com/document/product/436/7751
 */
public class QCloudCosTemplate {
    private final COS cosClient;
    private final OssProperties ossProperties;

    public QCloudCosTemplate(COS cosClient, OssProperties ossProperties) {
        this.cosClient = cosClient;
        this.ossProperties = ossProperties;
    }

    /**
     * 根据规则生成存储桶名称，腾讯 COS 存储桶后必须添加 appId
     *
     * @return 存储桶名称
     */
    protected String getBucketName() {
        return getBucketName()
                .concat("-")
                .concat(ossProperties.getTencent()
                        .getAppId());
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    public void createBucket(String bucketName) {
        if (!bucketExist(bucketName)) {
            cosClient.createBucket(bucketName);
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
    }

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    public void deleteBucket(String bucketName) {
        if (bucketExist(bucketName)) {
            cosClient.deleteBucket(bucketName);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExist(String bucketName) {
        return cosClient.doesBucketExist(bucketName);
    }

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param key         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    public CopyObjectResult copyObject(String sourceBucketName, String key, String targetBucketName) {
        return copyObject(sourceBucketName, key, targetBucketName, key);
    }

    /**
     * 拷贝文件，重命名
     *
     * @param sourceBucketName      源存储桶名称
     * @param sourceKey             存储桶文件名称
     * @param destinationBucketName 目标存储桶名称
     * @param destinationKey        目标存储桶文件名称
     */
    public CopyObjectResult copyObject(String sourceBucketName, String sourceKey,
                                       String destinationBucketName, String destinationKey) {
        return cosClient.copyObject(sourceBucketName, sourceKey,
                destinationBucketName, destinationKey);
    }

    /**
     * 获取文件元信息
     *
     * @param key 存储桶文件名称
     * @return 文件元信息
     */
    public ObjectMetadata getMetadata(String key) {
        return getMetadata(getBucketName(), key);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param key   存储桶文件名称
     * @return 文件元信息
     */
    public ObjectMetadata getMetadata(String bucketName, String key) {
        return cosClient.getObjectMetadata(bucketName, key);
    }

    /**
     * 获取 COS 域名地址
     *
     * @return 域名地址
     */
    private String getEndpoint() {
        return getEndpoint(ossProperties.getTencent().getBucketName());
    }

    /**
     * 获取 COS 域名地址
     *
     * @param bucketName 存储桶名称
     * @return 域名地址
     */
    private String getEndpoint(String bucketName) {
        OssProperties.QcloudCosProperties properties = ossProperties.getTencent();

        return String.format("%s://%s",
                properties.getScheme(),
                cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(bucketName));
    }

    /**
     * 获取文件相对路径
     *
     * @param key 存储桶对象名称
     * @return 文件相对路径
     */
    public String getObjectPath(String key) {
        return getEndpoint()
                .concat("/")
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
        return getEndpoint(bucketName)
                .concat("/")
                .concat(key);
    }

    /**
     * 获取文件地址
     *
     * @param key 存储桶对象名称
     * @return 文件地址
     */
    public String getObjectLink(String key) {
        return getEndpoint()
                .concat("/")
                .concat(key);
    }

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @return 文件地址
     */
    public String getObjectLink(String bucketName, String key) {
        return getEndpoint(bucketName)
                .concat("/")
                .concat(key);
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
        return upload(key, file);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        上传文件名
     * @param file       上传文件类
     * @return 文件信息
     */
    public PutObjectResult upload(String bucketName, String key, MultipartFile file) throws IOException {
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
        return upload(getBucketName(), key, stream);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    public PutObjectResult upload(String bucketName, String key, InputStream stream) {
        return upload(bucketName, key, stream, null);
    }

    private PutObjectResult upload(String bucketName, String key, InputStream stream, ObjectMetadata metadata) {
        return cosClient.putObject(bucketName, key, stream, metadata);
    }

    /**
     * 删除文件
     *
     * @param key 存储桶对象名称
     */
    public void deleteObject(String key) {
        cosClient.deleteObject(getBucketName(), key);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     */
    public void deleteObject(String bucketName, String key) {
        cosClient.deleteObject(bucketName, key);
    }

    /**
     * 批量删除文件
     *
     * @param keys 存储桶对象名称集合
     */
    public DeleteObjectsResult deleteFiles(String bucketName, String... keys) {
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName).withKeys(keys);
        return cosClient.deleteObjects(request);
    }
}
