package com.fight2048.oss.autoconfigure.qiniu;

import com.fight2048.oss.OssProperties;
import com.fight2048.oss.Utils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.AclType;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2020-03-07 0007 下午 10:46
 * @version: v0.0.0
 * @description: <p>
 * 七牛云操作，参考文档：https://developer.qiniu.com/kodo/1239/java
 * </p>
 */
public class QiniuCloudTemplate {
    private final Auth auth;
    private final UploadManager uploadManager;
    private final BucketManager bucketManager;
    private final OssProperties ossProperties;

    public QiniuCloudTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, OssProperties ossProperties) {
        this.auth = auth;
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
        this.ossProperties = ossProperties;
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    public void createBucket(String bucketName) throws QiniuException {
        if (!bucketExist(bucketName)) {
            bucketManager.createBucket(bucketName, ossProperties.getQiniu().getRegion());
            bucketManager.putBucketAccessMode(bucketName, AclType.PUBLIC);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    public boolean bucketExist(String bucketName) throws QiniuException {
        final String[] buckets = bucketManager.buckets();
        return Utils.contains(buckets, bucketName);
    }

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param key              存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    public Response copyObject(String sourceBucketName, String key, String targetBucketName) {
        return copyObject(sourceBucketName, key, targetBucketName, key);
    }

    /**
     * 拷贝文件，重命名
     *
     * @param sourceBucketName 源存储桶名称
     * @param sourceKey        存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     * @param targetKey        目标存储桶文件名称
     */
    @SneakyThrows
    public Response copyObject(String sourceBucketName, String sourceKey, String targetBucketName, String targetKey) {
        return bucketManager.copy(sourceBucketName, sourceKey, targetBucketName, targetKey);
    }

    /**
     * 获取文件元信息
     *
     * @param key 存储桶文件名称
     * @return 文件元信息
     */
    public FileInfo getMetadata(String key) throws QiniuException {
        return getMetadata(getBucketName(), key);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶文件名称
     * @return 文件元信息
     */
    public FileInfo getMetadata(String bucketName, String key) throws QiniuException {
        return bucketManager.stat(bucketName, key);
    }


    /**
     * 上传文件
     *
     * @param file 上传文件类
     * @return 文件信息
     */
    public Response upload(MultipartFile file) throws IOException {
        return upload(file.getOriginalFilename(), file);
    }

    /**
     * 上传文件
     *
     * @param key  上传文件名
     * @param file 上传文件类
     * @return 文件信息
     */
    public Response upload(String key, MultipartFile file) throws IOException {
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
    public Response upload(String bucketName, String key, MultipartFile file) throws IOException {
        return upload(bucketName, key, file.getInputStream());
    }

    /**
     * 上传文件
     *
     * @param key    存储桶对象名称
     * @param stream 文件流
     * @return 文件信息
     */
    public Response upload(String key, InputStream stream) throws QiniuException {
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
    public Response upload(String bucketName, String key, InputStream stream) throws QiniuException {
        return upload(bucketName, key, stream, null, null);
    }

    private Response upload(String bucketName, String key,
                            InputStream stream, StringMap params, String mime) throws QiniuException {
        return uploadManager.put(stream, key, getUploadToken(bucketName, key), params, mime);
    }

    /**
     * 删除文件
     *
     * @param key 存储桶对象名称
     */
    public void deleteObject(String key) throws QiniuException {
        bucketManager.delete(getBucketName(), key);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     */
    public void deleteObject(String bucketName, String key) throws QiniuException {
        bucketManager.delete(bucketName, key);
    }

    /**
     * 批量删除文件
     *
     * @param keys 存储桶对象名称集合
     */
    public Response deleteObjects(String... keys) throws QiniuException {
        return deleteObjects(getBucketName(), keys);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param keys       存储桶对象名称集合
     */
    public Response deleteObjects(String bucketName, String... keys) throws QiniuException {
        //创建 BatchOperations 类型的 operations 对象
        BucketManager.BatchOperations operations = new BucketManager
                .BatchOperations()
                .addDeleteOp(bucketName, keys);
        return bucketManager.batch(operations);
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @return Token
     */
    private String getUploadToken(String bucketName) {
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取上传凭证，覆盖上传
     *
     * @param bucketName 存储桶名称
     * @param key        文件名
     * @return Token
     */
    private String getUploadToken(String bucketName, String key) {
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    private String getBucketName() {
        return ossProperties.getQiniu().getBucketName();
    }
}
