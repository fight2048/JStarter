package cn.itsite.oss.autoconfigure;

import cn.itsite.oss.model.Kv;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
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

    /**
     * 阿里云 OSS 配置
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AliyunOssProperties extends CommonProperties {
        /**
         * 是否使用 https
         */
        private Boolean https = false;
    }

    /**
     * 腾讯云 OSS 配置
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
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
    }

    /**
     * 七牛云存储配置
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class QiniuProperties extends CommonProperties {
        /**
         * 区域简称，https://developer.qiniu.com/kodo/manual/1671/region-endpoint
         */
        private String region;
    }

    /**
     * MinIO 配置
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class MinIoProperties extends CommonProperties {
    }

    /**
     * 通用配置
     */
    @Data
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
        private Kv args;
    }
}
