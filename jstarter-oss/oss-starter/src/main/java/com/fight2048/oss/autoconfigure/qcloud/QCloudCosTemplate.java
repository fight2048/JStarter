package com.fight2048.oss.autoconfigure.qcloud;

import com.fight2048.oss.OssProperties;
import com.qcloud.cos.COS;

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
    public String getBucketName() {
        return getBucketName()
                .concat("-")
                .concat(ossProperties.getTencent()
                        .getAppId());
    }


    /**
     * 获取 COS 域名地址
     *
     * @return 域名地址
     */
    public String getEndpoint() {
        return getEndpoint(ossProperties.getTencent()
                .getBucketName());
    }

    /**
     * 获取 COS 域名地址
     *
     * @param bucketName 存储桶名称
     * @return 域名地址
     */
    public String getEndpoint(String bucketName) {
        OssProperties.QcloudCosProperties properties = ossProperties.getTencent();
        return properties.getScheme()
                + cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(bucketName);
    }
}
