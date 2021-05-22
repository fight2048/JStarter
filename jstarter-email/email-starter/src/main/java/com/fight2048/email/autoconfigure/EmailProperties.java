package com.fight2048.email.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = EmailProperties.EMAIL_PREFIX)
public class EmailProperties {
    public static final String EMAIL_PREFIX = "email";

    /**
     * 阿里云 OSS
     */
    private AliyunEmailProperties aliyunEmail = new AliyunEmailProperties();

    public AliyunEmailProperties getAliyunEmail() {
        return aliyunEmail;
    }

    public void setAliyunEmail(AliyunEmailProperties aliyunEmail) {
        this.aliyunEmail = aliyunEmail;
    }

    /**
     * 阿里云 OSS 配置
     */
    public static class AliyunEmailProperties extends CommonProperties {
        private String accessKey;
        private String secretKey;
        private String regionId;
        private String endpoint;
        private String product;
        private String domain;

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

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }
    }

    /**
     * 通用配置
     */
    public static class CommonProperties {
    }
}
