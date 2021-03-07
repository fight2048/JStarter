package cn.itsite.oss.autoconfigure.qiniu;

import cn.itsite.oss.DefaultOssTemplate;
import cn.itsite.oss.autoconfigure.OssAutoConfiguration;
import cn.itsite.oss.autoconfigure.OssProperties;
import cn.itsite.oss.support.qiniu.QiniuCloudTemplate;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
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
@ConditionalOnProperty(value = "oss.qiniu-cloud.enabled", havingValue = "true")
public class QiniuCloudAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(com.qiniu.storage.Configuration.class)
    public com.qiniu.storage.Configuration qnConfiguration() {
        return new com.qiniu.storage.Configuration(Zone.autoZone());
    }

    @Bean
    public Auth auth(OssProperties ossProperties) {
        OssProperties.QiniuProperties properties = ossProperties.getQiniuCloud();
        return Auth.create(properties.getAccessKey(), properties.getSecretKey());
    }

    @Bean
    @ConditionalOnBean(com.qiniu.storage.Configuration.class)
    public UploadManager uploadManager(com.qiniu.storage.Configuration configuration) {
        return new UploadManager(configuration);
    }

    @Bean
    @ConditionalOnBean(com.qiniu.storage.Configuration.class)
    public BucketManager bucketManager(com.qiniu.storage.Configuration configuration, OssProperties ossProperties) {
        return new BucketManager(auth(ossProperties), configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({Auth.class, UploadManager.class, BucketManager.class})
    public QiniuCloudTemplate qiniuCloudTemplate(Auth auth,
                                                 UploadManager uploadManager,
                                                 BucketManager bucketManager,
                                                 OssProperties ossProperties) {
        return new QiniuCloudTemplate(auth, uploadManager, bucketManager, ossProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({QiniuCloudTemplate.class})
    public DefaultOssTemplate qiniuOssTemplate(QiniuCloudTemplate template) {
        return new DefaultOssTemplate(template);
    }
}
