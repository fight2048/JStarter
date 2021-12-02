package com.fight2048.sms.api;

import com.fight2048.sms.SmsTemplate;
import com.fight2048.sms.util.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @version: v0.0.0
 * @blog: https://github.com/fight2048
 * @time: 2019/10/24/0024 0:25
 * @description:
 */
@RestController
public class SmsController {
    @Autowired
    SmsTemplate smsTemplate;

    @GetMapping("/aliyun/sms")
    public Object aliyunSms(String phone, String sign, String template, String parameters, String outId) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>() {{
            put("code", parameters);
        }};

        String params = JsonHelper.object2String(map);
        return smsTemplate.sendSms(phone, sign, template, params, outId);
    }
}