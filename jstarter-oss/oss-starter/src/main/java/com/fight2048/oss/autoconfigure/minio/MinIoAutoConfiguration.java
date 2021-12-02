package com.fight2048.oss.autoconfigure.minio;

import com.fight2048.oss.DefaultOssTemplate;
import com.fight2048.oss.autoconfigure.OssAutoConfiguration;
import com.fight2048.oss.autoconfigure.OssProperties;
import com.fight2048.oss.support.minio.MinIoTemplate;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
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
@ConditionalOnProperty(value = "oss.min-io.enabled", havingValue = "true")
public class MinIoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(OssProperties ossProperties) throws InvalidPortException, InvalidEndpointException {
        OssProperties.MinIoProperties properties = ossProperties.getMinIo();
        return new MinioClient(properties.getEndpoint(),
                properties.getAccessKey(),
                properties.getSecretKey());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({MinioClient.class})
    public MinIoTemplate minIoTemplate(MinioClient minioClient, OssProperties ossProperties) {
        return new MinIoTemplate(minioClient, ossProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({MinIoTemplate.class})
    public DefaultOssTemplate minIoOssTemplate(MinIoTemplate template) {
        return new DefaultOssTemplate(template);
    }
}
