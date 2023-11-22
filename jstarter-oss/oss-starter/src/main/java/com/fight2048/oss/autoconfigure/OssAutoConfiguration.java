package com.fight2048.oss.autoconfigure;

import com.fight2048.oss.autoconfigure.aliyun.AliyunOssTemplate;
import com.fight2048.oss.autoconfigure.qcloud.QCloudCosTemplate;
import com.fight2048.oss.autoconfigure.qiniu.QiniuCloudTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class OssAutoConfiguration {

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean({AliyunOssTemplate.class})
//    public DefaultOssTemplate defaultOssTemplate(AliyunOssTemplate template) {
//        return new DefaultOssTemplate(template);
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean({MinIoTemplate.class})
//    @ConditionalOnProperty(value = "oss.min-io.enabled", havingValue = "true")
//    public DefaultOssTemplate minIoOssTemplate(MinIoTemplate template) {
//        return new DefaultOssTemplate(template);
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean({QiniuCloudTemplate.class})
//    @ConditionalOnProperty(value = "oss.qiniu-cloud.enabled", havingValue = "true")
//    public DefaultOssTemplate qiniuOssTemplate(QiniuCloudTemplate template) {
//        return new DefaultOssTemplate(template);
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnBean({QCloudCosTemplate.class})
//    @ConditionalOnProperty(value = "oss.qcloud-cos.enabled", havingValue = "true")
//    public DefaultOssTemplate qcloudOssTemplate(QCloudCosTemplate template) {
//        return new DefaultOssTemplate(template);
//    }
}
