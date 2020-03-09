package cn.itsite.oss.autoconfigure;

import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
//@ConditionalOnProperty(value = "email.aliyun-email.enabled", havingValue = "true")
public class AliyunEmailAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass({SingleSendMailRequest.class})
    public AliyunEamilTemplate aliyunEamilTemplate(EmailProperties properties) {
        return new AliyunEamilTemplate(properties);
    }
}
