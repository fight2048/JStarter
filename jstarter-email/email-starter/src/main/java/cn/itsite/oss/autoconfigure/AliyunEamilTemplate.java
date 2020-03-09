package cn.itsite.oss.autoconfigure;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Objects;

/**
 * 阿里云 邮箱SDK 参考文档：https://help.aliyun.com/document_detail/29459.html
 */
public class AliyunEamilTemplate {
    private final EmailProperties emailProperties;

    public AliyunEamilTemplate(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    public SingleSendMailResponse send(SingleSendMailRequest request) throws ClientException {
        // 如果是除杭州region外的其它region（如新加坡、澳洲Region），需要将下面的”cn-hangzhou”替换为”ap-southeast-1”、或”ap-southeast-2”。
        EmailProperties.AliyunEmailProperties properties = emailProperties.getAliyunEmail();
        IClientProfile profile = DefaultProfile.getProfile(properties.getRegionId(),
                properties.getAccessKey(),
                properties.getSecretKey());
        // 如果是除杭州region外的其它region（如新加坡region）， 需要做如下处理
        if (Objects.nonNull(properties.getEndpoint())
                || Objects.nonNull(properties.getProduct())
                || Objects.nonNull(properties.getDomain())) {
            DefaultProfile.addEndpoint(properties.getEndpoint(),
                    properties.getRegionId(),
                    properties.getProduct(),
                    properties.getDomain());
        }
        IAcsClient client = new DefaultAcsClient(profile);
        return client.getAcsResponse(request);
    }
}
