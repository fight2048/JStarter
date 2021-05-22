package com.fight2048.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2021-05-22 0022 下午 9:44
 * @version: v0.0.0
 * @description: 阿里云 短信SDK 参考文档：https://help.aliyun.com/document_detail/215759.html
 */
@ConfigurationProperties(prefix = SmsProperties.EMAIL_PREFIX)
public class SmsProperties {
    public static final String EMAIL_PREFIX = "sms";

    /**
     * 阿里云 SMS
     */
    private AliyunSmsProperties aliyun = new AliyunSmsProperties();

    public AliyunSmsProperties getAliyun() {
        return aliyun;
    }

    public void setAliyun(AliyunSmsProperties aliyun) {
        this.aliyun = aliyun;
    }

    /**
     * 阿里云 SMS 配置
     */
    public static class AliyunSmsProperties {
        private boolean enabled;
        private String accessKeyId;
        private String accessKeySecret;
        private String endpoint;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
