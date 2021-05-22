package com.fight2048.sms.support.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.fight2048.sms.SmsProperties;
import com.fight2048.sms.SmsResponse;
import com.fight2048.sms.SmsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2021-05-22 0022 下午 9:44
 * @version: v0.0.0
 * @description: 阿里云 短信SDK 参考文档：https://help.aliyun.com/document_detail/215759.html
 */
public class AliyunSmsTemplate implements SmsTemplate {
    private Logger log = LoggerFactory.getLogger(AliyunSmsTemplate.class);
    private final SmsProperties properties;
    private static final Map<String, Integer> codeMap = new HashMap<String, Integer>() {{
        put("OK", SmsResponse.SMS_OK);
        put("isp.RAM_PERMISSION_DENY", SmsResponse.RAM_PERMISSION_DENY);
        put("isv.OUT_OF_SERVICE", SmsResponse.OUT_OF_SERVICE);
        put("isv.PRODUCT_UN_SUBSCRIPT", SmsResponse.PRODUCT_UN_SUBSCRIPT);
        put("isv.PRODUCT_UNSUBSCRIBE", SmsResponse.PRODUCT_UNSUBSCRIBE);
        put("isv.ACCOUNT_NOT_EXISTS", SmsResponse.ACCOUNT_NOT_EXISTS);
        put("isv.ACCOUNT_ABNORMAL", SmsResponse.ACCOUNT_ABNORMAL);
        put("isv.SMS_TEMPLATE_ILLEGAL", SmsResponse.SMS_TEMPLATE_ILLEGAL);
        put("isv.SMS_SIGNATURE_ILLEGAL", SmsResponse.SMS_SIGNATURE_ILLEGAL);
        put("isv.INVALID_PARAMETERS", SmsResponse.INVALID_PARAMETERS);
        put("isp.SYSTEM_ERROR", SmsResponse.SYSTEM_ERROR);
        put("isv.MOBILE_NUMBER_ILLEGAL", SmsResponse.MOBILE_NUMBER_ILLEGAL);
        put("isv.MOBILE_COUNT_OVER_LIMIT", SmsResponse.MOBILE_COUNT_OVER_LIMIT);
        put("isv.TEMPLATE_MISSING_PARAMETERS", SmsResponse.TEMPLATE_MISSING_PARAMETERS);
        put("isv.BUSINESS_LIMIT_CONTROL", SmsResponse.BUSINESS_LIMIT_CONTROL);
        put("isv.INVALID_JSON_PARAM", SmsResponse.INVALID_JSON_PARAM);
        put("isv.BLACK_KEY_CONTROL_LIMIT", SmsResponse.BLACK_KEY_CONTROL_LIMIT);
        put("isv.PARAM_LENGTH_LIMIT", SmsResponse.PARAM_LENGTH_LIMIT);
        put("isv.PARAM_NOT_SUPPORT_URL", SmsResponse.PARAM_NOT_SUPPORT_URL);
        put("isv.AMOUNT_NOT_ENOUGH", SmsResponse.AMOUNT_NOT_ENOUGH);
    }};

    public AliyunSmsTemplate(SmsProperties properties) {
        this.properties = properties;
    }

    @Override
    public SmsResponse sendSms(String phone, String smsName, String template, String param, String outId) throws Exception {
        log.info("phone {}, smsName {}, template {}, param {}, outId {}", phone, smsName, template, param, outId);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(template);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(param);
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId(outId);

        SmsProperties.AliyunSmsProperties smsProperties = properties.getAliyun();

        Config config = new Config();
        if (Objects.nonNull(smsProperties.getEndpoint())) {
            // 访问的域名
            config.setEndpoint(smsProperties.getEndpoint());
        }

        if (Objects.nonNull(smsProperties.getAccessKeyId())) {
            // 您的AccessKey ID
            config.setAccessKeyId(smsProperties.getAccessKeyId());
        }
        if (Objects.nonNull(smsProperties.getAccessKeySecret())) {
            // 您的AccessKey Secret
            config.setAccessKeySecret(smsProperties.getAccessKeySecret());
        }

        SendSmsResponse response = new Client(config).sendSms(request);
        SendSmsResponseBody body = response.getBody();
        Integer code = codeMap.get(body.getCode());

        SmsResponse smsResponse = new SmsResponse();
        if (Objects.nonNull(code)) {
            smsResponse.setCode(code);
        } else {
            smsResponse.setCode(SmsResponse.UNKONW_ERROR);
        }

        smsResponse.setMessage(body.getMessage());
        smsResponse.setUid(body.getBizId());
        smsResponse.setRequestId(body.getRequestId());
        log.info("response-->{}", smsResponse);
        return smsResponse;
    }
}
