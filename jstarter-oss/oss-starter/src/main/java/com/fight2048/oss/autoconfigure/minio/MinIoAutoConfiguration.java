package com.fight2048.oss.autoconfigure.minio;

import com.fight2048.oss.OssProperties;
import com.fight2048.oss.autoconfigure.OssAutoConfiguration;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2020-03-07 0007 下午 10:46
 * @version: v0.0.0
 * @description:
 */
@Configuration
@AutoConfigureBefore(OssAutoConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.minio.enabled", havingValue = "true")
public class MinIoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(OssProperties ossProperties) {
        OssProperties.MinIoProperties properties = ossProperties.getMinio();
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({MinioClient.class})
    public MinIoTemplate minIoTemplate(MinioClient minioClient, OssProperties ossProperties) {
        return new MinIoTemplate(minioClient, ossProperties);
    }
}
