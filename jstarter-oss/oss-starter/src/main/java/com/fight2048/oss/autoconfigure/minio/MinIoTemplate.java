package com.fight2048.oss.autoconfigure.minio;

import com.fight2048.oss.OssProperties;
import io.minio.MinioClient;

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
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    public String getBucketName() {
        return ossProperties.getMinio().getBucketName();
    }
}
