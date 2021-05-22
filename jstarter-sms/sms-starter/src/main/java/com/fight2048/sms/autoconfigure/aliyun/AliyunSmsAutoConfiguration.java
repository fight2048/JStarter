package com.fight2048.sms.autoconfigure.aliyun;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.fight2048.sms.support.aliyun.AliyunSmsTemplate;
import com.fight2048.sms.SmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2021-05-22 0022 下午 9:44
 * @version: v0.0.0
 * @description: 阿里云 短信SDK 参考文档：https://help.aliyun.com/document_detail/215759.html
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.aliyun.enabled", havingValue = "true")
public class AliyunSmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({SendSmsRequest.class})
    public AliyunSmsTemplate aliyunSmsTemplate(SmsProperties properties) {
        return new AliyunSmsTemplate(properties);
    }
}
