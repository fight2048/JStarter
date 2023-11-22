package com.fight2048.oss.autoconfigure.minio;

import com.fight2048.oss.OssProperties;
import com.fight2048.oss.Utils;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2020-03-07 0007 下午 10:46
 * @version: v0.0.0
 * @description: <p>
 * MinIO 操作，参考文档：https://min.io/docs/minio/linux/developers/java/API.html
 * </p>
 */
public class MinIoTemplate {
    private final MinioClient minioClient;
    private final OssProperties ossProperties;

    public MinIoTemplate(MinioClient minioClient, OssProperties ossProperties) {
        this.minioClient = minioClient;
        this.ossProperties = ossProperties;
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!bucketExist(bucketName)) {
            MakeBucketArgs args = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(args);
        }
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public void createBucket(String bucketName, String region) {
        if (!bucketExist(bucketName)) {
            MakeBucketArgs args = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .region(region)
                    .build();
            minioClient.makeBucket(args);
        }
    }

    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    public void deleteBucket(String bucketName) {
        if (bucketExist(bucketName)) {
            RemoveBucketArgs args = RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.removeBucket(args);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    @SneakyThrows
    public boolean bucketExist(String bucketName) {
        BucketExistsArgs args = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        return minioClient.bucketExists(args);
    }

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param key              存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    public ObjectWriteResponse copyObject(String sourceBucketName, String key, String targetBucketName) {
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
    public ObjectWriteResponse copyObject(String sourceBucketName, String sourceKey, String targetBucketName, String targetKey) {
        CopyObjectArgs args = CopyObjectArgs.builder()
                .bucket(targetBucketName)
                .object(targetKey)
                .source(CopySource.builder()
                        .bucket(sourceBucketName)
                        .object(sourceKey)
                        .build()
                ).build();
        return minioClient.copyObject(args);
    }

    /**
     * 获取文件元信息
     *
     * @param fileName 存储桶文件名称
     * @return 文件元信息
     */
    public StatObjectResponse getMetadata(String fileName) {
        return getMetadata(getBucketName(), fileName);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶文件名称
     * @return 文件元信息
     */
    @SneakyThrows
    public StatObjectResponse getMetadata(String bucketName, String key) {
        StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucketName)
                .object(key)
                .build();
        return minioClient.statObject(args);
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
        return getObjectLink(ossProperties.getMinio()
                .getEndpoint(), key);
    }

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @return 文件地址
     */
    public String getObjectLink(String bucketName, String key) {
        return ossProperties.getMinio()
                .getEndpoint()
                .concat("/")
                .concat(bucketName)
                .concat("/")
                .concat(key);
    }

    /**
     * 上传文件
     *
     * @param file 上传文件类
     * @return 文件信息
     */
    public ObjectWriteResponse upload(MultipartFile file) {
        return upload(file.getOriginalFilename(), file);
    }

    /**
     * 上传文件
     *
     * @param key  上传文件名
     * @param file 上传文件类
     * @return 文件信息
     */
    @SneakyThrows
    public ObjectWriteResponse upload(String key, MultipartFile file) {
        String bucketName = ossProperties.getMinio().getBucketName();
        return upload(bucketName, key, file.getInputStream(), file.getSize());
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    @SneakyThrows
    public ObjectWriteResponse upload(String bucketName, String key, InputStream stream, long objectSize) {
        // 创建存储桶
        createBucket(bucketName);

        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(key)
                .stream(stream, objectSize, -1)
                .contentType(Utils.mimeType(key))
                .build();
        return minioClient.putObject(args);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    @SneakyThrows
    public ObjectWriteResponse upload(String bucketName, String region, String key, InputStream stream, long objectSize) {
        // 创建存储桶
        createBucket(bucketName, region);

        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .region(region)
                .object(key)
                .stream(stream, objectSize, -1)
                .contentType(Utils.mimeType(key))
                .build();
        return minioClient.putObject(args);
    }

    /**
     * 删除文件
     *
     * @param key 存储桶对象名称
     */
    @SneakyThrows
    public void deleteObject(String key) {
        deleteObject(getBucketName(), key);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param key        存储桶对象名称
     */
    @SneakyThrows
    public void deleteObject(String bucketName, String key) {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(key)
                .build();
        minioClient.removeObject(args);
    }

    /**
     * 批量删除文件
     *
     * @param keys 存储桶对象名称集合
     */
    public Iterable<Result<DeleteError>> deleteObjects(List<String> keys) {
        return deleteObjects(getBucketName(), keys);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param keys       存储桶对象名称集合
     */
    public Iterable<Result<DeleteError>> deleteObjects(String bucketName, List<String> keys) {
        List<DeleteObject> objects = keys.stream()
                .map(key -> new DeleteObject(key))
                .toList();

        RemoveObjectsArgs args = RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(objects)
                .build();
        return minioClient.removeObjects(args);
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    private String getBucketName() {
        return ossProperties.getMinio().getBucketName();
    }
}
