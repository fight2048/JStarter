package cn.itsite.oss.autoconfigure.minio;

import cn.itsite.oss.DefaultOssTemplate;
import cn.itsite.oss.autoconfigure.OssAutoConfiguration;
import cn.itsite.oss.autoconfigure.OssProperties;
import cn.itsite.oss.support.minio.MinIoTemplate;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureBefore(OssAutoConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.min-io.enabled", havingValue = "true")
public class MinIoAutoConfiguration {

    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean
    public MinioClient minioClient(OssProperties ossProperties) {
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
