package cn.itsite.oss.autoconfigure;

import cn.itsite.oss.DefaultOssTemplate;
import cn.itsite.oss.support.aliyun.AliyunOssTemplate;
import cn.itsite.oss.support.minio.MinIoTemplate;
import cn.itsite.oss.support.qcloud.QCloudCosTemplate;
import cn.itsite.oss.support.qiniu.QiniuCloudTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({AliyunOssTemplate.class})

    public DefaultOssTemplate defaultOssTemplate(AliyunOssTemplate template) {
        return new DefaultOssTemplate(template);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({MinIoTemplate.class})
    @ConditionalOnProperty(value = "oss.min-io.enabled", havingValue = "true")
    public DefaultOssTemplate minIoOssTemplate(MinIoTemplate template) {
        return new DefaultOssTemplate(template);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({QiniuCloudTemplate.class})
    @ConditionalOnProperty(value = "oss.qiniu-cloud.enabled", havingValue = "true")
    public DefaultOssTemplate qiniuOssTemplate(QiniuCloudTemplate template) {
        return new DefaultOssTemplate(template);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({QCloudCosTemplate.class})
    @ConditionalOnProperty(value = "oss.qcloud-cos.enabled", havingValue = "true")
    public DefaultOssTemplate qcloudOssTemplate(QCloudCosTemplate template) {
        return new DefaultOssTemplate(template);
    }
}
