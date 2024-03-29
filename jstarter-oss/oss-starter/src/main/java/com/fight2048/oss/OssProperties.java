package com.fight2048.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = OssProperties.OSS_PREFIX)
public class OssProperties {
    public static final String OSS_PREFIX = "oss";

    /**
     * 通用配置
     */
    public static class CommonProperties {
        /**
         * 是否启用
         */
        private boolean enabled = false;

        /**
         * 对象存储服务的URL
         */
        private String endpoint;

        /**
         * Access key
         */
        private String accessKey;

        /**
         * Secret key
         */
        private String secretKey;

        /**
         * 默认的存储桶名称
         */
        private String bucketName = OSS_PREFIX;

        /**
         * 自定义属性
         */
        private KeyValue extension;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }


        public KeyValue getExtension() {
            return extension;
        }

        public void setExtension(KeyValue extension) {
            this.extension = extension;
        }
    }

    /**
     * 亚马逊云AWS OSS
     */
    private AwsOssProperties aws = new AwsOssProperties();

    /**
     * 阿里云 OSS
     */
    private AliyunOssProperties aliyun = new AliyunOssProperties();
    /**
     * 腾讯云 COS
     */
    private QcloudCosProperties tencent = new QcloudCosProperties();
    /**
     * 七牛云OSS
     */
    private QiniuProperties qiniu = new QiniuProperties();
    /**
     * MinIO OSS
     */
    private MinIoProperties minio = new MinIoProperties();

    public AwsOssProperties getAws() {
        return aws;
    }

    public void setAws(AwsOssProperties aws) {
        this.aws = aws;
    }

    public AliyunOssProperties getAliyun() {
        return aliyun;
    }

    public void setAliyun(AliyunOssProperties aliyun) {
        this.aliyun = aliyun;
    }

    public QcloudCosProperties getTencent() {
        return tencent;
    }

    public void setTencent(QcloudCosProperties tencent) {
        this.tencent = tencent;
    }

    public QiniuProperties getQiniu() {
        return qiniu;
    }

    public void setQiniu(QiniuProperties qiniu) {
        this.qiniu = qiniu;
    }

    public MinIoProperties getMinio() {
        return minio;
    }

    public void setMinio(MinIoProperties minio) {
        this.minio = minio;
    }

    /**
     * 亚马逊云AWS OSS 配置
     */
    public static class AwsOssProperties extends CommonProperties {
        /**
         * 区域简称，https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/CreatingMultiRegionAccessPoints.html
         */
        private String region;

        /**
         * true path-style nginx 反向代理和S3默认支持 pathStyle模式 {http://endpoint/bucketname}
         * false supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style 模式{http://bucketname.endpoint}
         * 只是url的显示不一样
         */
        private Boolean pathStyleAccess = true;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public Boolean getPathStyleAccess() {
            return pathStyleAccess;
        }

        public void setPathStyleAccess(Boolean pathStyleAccess) {
            this.pathStyleAccess = pathStyleAccess;
        }
    }

    /**
     * 阿里云 OSS 配置
     */
    public static class AliyunOssProperties extends CommonProperties {
        /**
         * 使用协议，如http/https
         */
        private String scheme = "https";

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }
    }

    /**
     * 腾讯云 OSS 配置
     */
    public static class QcloudCosProperties extends CommonProperties {
        /**
         * App Id
         */
        private String appId;

        /**
         * 区域简称，https://cloud.tencent.com/document/product/436/6224
         */
        private String region;

        /**
         * 使用协议，如http/https
         */
        private String scheme = "https";

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

    }

    /**
     * 七牛云存储配置
     */
    public static class QiniuProperties extends CommonProperties {
        /**
         * 区域简称，https://developer.qiniu.com/kodo/1671/region-endpoint-fq
         */
        private String region;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

    /**
     * MinIO 配置
     */
    public static class MinIoProperties extends CommonProperties {
    }
}
