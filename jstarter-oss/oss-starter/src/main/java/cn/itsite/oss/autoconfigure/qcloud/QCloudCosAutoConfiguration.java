package cn.itsite.oss.autoconfigure.qcloud;

import cn.itsite.oss.DefaultOssTemplate;
import cn.itsite.oss.autoconfigure.OssAutoConfiguration;
import cn.itsite.oss.autoconfigure.OssProperties;
import cn.itsite.oss.support.qcloud.QCloudCosTemplate;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
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
@ConditionalOnProperty(value = "oss.qcloud-cos.enabled", havingValue = "true")
@EnableConfigurationProperties(OssProperties.class)
public class QCloudCosAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public COSClient cosClient(OssProperties ossProperties) {
        // 1 初始化用户身份信息（secretId, secretKey）
        OssProperties.QcloudCosProperties properties = ossProperties.getQcloudCos();
        COSCredentials credentials = new BasicCOSCredentials(properties.getAccessKey(),
                properties.getSecretKey());

        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region(properties.getRegion());

        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        ClientConfig clientConfig = new ClientConfig(region);
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        clientConfig.setMaxConnectionsCount(1024);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        clientConfig.setSocketTimeout(50000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        clientConfig.setConnectionTimeout(50000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        clientConfig.setConnectionRequestTimeout(1000);
        return new COSClient(credentials, clientConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({COSClient.class})
    public QCloudCosTemplate qcloudCosTemplate(COSClient cosClient, OssProperties ossProperties) {
        return new QCloudCosTemplate(cosClient, ossProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({QCloudCosTemplate.class})
    public DefaultOssTemplate qcloudOssTemplate(QCloudCosTemplate template) {
        return new DefaultOssTemplate(template);
    }
}
