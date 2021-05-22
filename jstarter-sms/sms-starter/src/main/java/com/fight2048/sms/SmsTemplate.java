package com.fight2048.sms;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2021-05-22 0022 下午 9:44
 * @version: v0.0.0
 * @description: SMS 接口
 */
public interface SmsTemplate {

    SmsResponse sendSms(String phone, String smsName, String template, String param, String outId) throws Exception;
}
