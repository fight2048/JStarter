package com.fight2048.sms.api;

import com.fight2048.sms.SmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2019/10/24/0024 0:25
 * @description:
 */
@RestController
public class SmsController {
    @Autowired
    SmsTemplate smsTemplate;

    @GetMapping("/aliyun/sms")
    public Object aliyunSms(String phone, String sign, String template, String parameters, String outId) throws Exception {
        return smsTemplate.sendSms(phone, sign, template, parameters, outId);
    }
}