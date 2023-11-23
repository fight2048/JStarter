package com.fight2048.oss.autoconfigure.aws;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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
@ConditionalOnProperty(value = "oss.aws.enabled", havingValue = "true")
public class AwsOssAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public AmazonS3 amazonS3(OssProperties ossProperties) {
        OssProperties.AwsOssProperties properties = ossProperties.getAws();
        // 客户端配置，主要是全局的配置信息
        ClientConfiguration configuration = new ClientConfiguration();
        // url以及region配置
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                properties.getEndpoint(), properties.getRegion());
        // 凭证配置
        AWSCredentials credentials = new BasicAWSCredentials(properties.getAccessKey(),
                properties.getSecretKey());
        AWSCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        // build amazonS3Client客户端
        return AmazonS3Client.builder()
                .withClientConfiguration(configuration)
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(provider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(properties.getPathStyleAccess())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({AmazonS3.class})
    public AwsOssTemplate awsOssTemplate(AmazonS3 amazonS3, OssProperties ossProperties) {
        return new AwsOssTemplate(amazonS3, ossProperties);
    }
}
