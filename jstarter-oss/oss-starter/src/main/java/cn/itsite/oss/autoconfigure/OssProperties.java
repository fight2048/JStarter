package cn.itsite.oss.autoconfigure;

import cn.itsite.oss.model.Kv;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = OssProperties.OSS_PREFIX)
public class OssProperties {
    public static final String OSS_PREFIX = "oss";

    /**
     * 阿里云 OSS
     */
    private AliyunOssProperties aliyunOss = new AliyunOssProperties();
    /**
     * 腾讯云 COS
     */
    private QcloudCosProperties qcloudCos = new QcloudCosProperties();
    /**
     * 七牛云
     */
    private QiniuProperties qiniuCloud = new QiniuProperties();
    /**
     * MinIO
     */
    private MinIoProperties minIo = new MinIoProperties();

    public AliyunOssProperties getAliyunOss() {
        return aliyunOss;
    }

    public void setAliyunOss(AliyunOssProperties aliyunOss) {
        this.aliyunOss = aliyunOss;
    }

    public QcloudCosProperties getQcloudCos() {
        return qcloudCos;
    }

    public void setQcloudCos(QcloudCosProperties qcloudCos) {
        this.qcloudCos = qcloudCos;
    }

    public QiniuProperties getQiniuCloud() {
        return qiniuCloud;
    }

    public void setQiniuCloud(QiniuProperties qiniuCloud) {
        this.qiniuCloud = qiniuCloud;
    }

    public MinIoProperties getMinIo() {
        return minIo;
    }

    public void setMinIo(MinIoProperties minIo) {
        this.minIo = minIo;
    }

    /**
     * 阿里云 OSS 配置
     */
    public static class AliyunOssProperties extends CommonProperties {
        /**
         * 是否使用 https
         */
        private Boolean https = false;

        public Boolean getHttps() {
            return https;
        }

        public void setHttps(Boolean https) {
            this.https = https;
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
         * 是否使用 https
         */
        private Boolean https = false;

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

        public Boolean getHttps() {
            return https;
        }

        public void setHttps(Boolean https) {
            this.https = https;
        }
    }

    /**
     * 七牛云存储配置
     */
    public static class QiniuProperties extends CommonProperties {
        /**
         * 区域简称，https://developer.qiniu.com/kodo/manual/1671/region-endpoint
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
        private Kv map;

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

        public Kv getMap() {
            return map;
        }

        public void setMap(Kv map) {
            this.map = map;
        }
    }
}
