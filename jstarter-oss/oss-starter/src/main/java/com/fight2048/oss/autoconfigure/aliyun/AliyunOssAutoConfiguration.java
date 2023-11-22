package com.fight2048.oss.autoconfigure.aliyun;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.fight2048.oss.OssProperties;
import com.fight2048.oss.autoconfigure.OssAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(OssAutoConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.aliyun.enabled", havingValue = "true")
public class AliyunOssAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public OSS oss(OssProperties ossProperties) {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration config = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        config.setMaxConnections(1024);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        config.setSocketTimeout(50 * 1000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        config.setConnectionTimeout(50 * 1000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        config.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        config.setIdleConnectionTime(60 * 1000);
        // 设置失败请求重试次数，默认为3次。
        config.setMaxErrorRetry(5);
        // 配置协议
        OssProperties.AliyunOssProperties properties = ossProperties.getAliyun();
        config.setProtocol(Protocol.valueOf(properties.getScheme().toUpperCase()));

        OSSClientBuilder builder = new OSSClientBuilder();
        return builder.build(properties.getEndpoint(),
                properties.getAccessKey(),
                properties.getSecretKey(),
                config);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({OSS.class})
    public AliyunOssTemplate aliyunOssTemplate(OSS ossClient, OssProperties ossProperties) {
        return new AliyunOssTemplate(ossClient, ossProperties);
    }
}
