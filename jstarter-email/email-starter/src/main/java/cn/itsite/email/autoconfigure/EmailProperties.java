package cn.itsite.email.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = EmailProperties.EMAIL_PREFIX)
public class EmailProperties {
    public static final String EMAIL_PREFIX = "email";

    /**
     * 阿里云 OSS
     */
    private AliyunEmailProperties aliyunEmail = new AliyunEmailProperties();

    /**
     * 阿里云 OSS 配置
     */
    @Data
    public static class AliyunEmailProperties extends CommonProperties {
        private String accessKey;
        private String secretKey;
        private String regionId;
        private String endpoint;
        private String product;
        private String domain;
    }

    /**
     * 通用配置
     */
    @Data
    public static class CommonProperties {
    }
}
