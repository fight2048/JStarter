package com.fight2048.oss.autoconfigure.qiniu;

import com.fight2048.oss.OssProperties;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

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
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @return Token
     */
    public String getUploadToken(String bucketName) {
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取上传凭证，覆盖上传
     *
     * @param bucketName 存储桶名称
     * @param key        文件名
     * @return Token
     */
    public String getUploadToken(String bucketName, String key) {
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    public String getBucketName() {
        return ossProperties.getQiniu().getBucketName();
    }
}
