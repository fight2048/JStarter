package com.fight2048.sms;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2021-05-22 0022 下午 9:44
 * @version: v0.0.0
 * @description: 统一响应模型
 */
public class SmsResponse {
    //未知错误
    public static final Integer UNKONW_ERROR = -1;
    //请求成功
    public static final Integer SMS_OK = 200;
    //RAM权限DENY
    public static final Integer RAM_PERMISSION_DENY = 701;
    //业务停机
    public static final Integer OUT_OF_SERVICE = 702;
    //未开通云通信产品的阿里云客户
    public static final Integer PRODUCT_UN_SUBSCRIPT = 703;
    //产品未开通
    public static final Integer PRODUCT_UNSUBSCRIBE = 704;
    //账户不存在
    public static final Integer ACCOUNT_NOT_EXISTS = 705;
    //账户异常
    public static final Integer ACCOUNT_ABNORMAL = 706;
    //短信模板不合法
    public static final Integer SMS_TEMPLATE_ILLEGAL = 707;
    //短信签名不合法
    public static final Integer SMS_SIGNATURE_ILLEGAL = 708;
    //参数异常
    public static final Integer INVALID_PARAMETERS = 709;
    //系统错误
    public static final Integer SYSTEM_ERROR = 710;
    //非法手机号
    public static final Integer MOBILE_NUMBER_ILLEGAL = 711;
    //手机号码数量超过限制
    public static final Integer MOBILE_COUNT_OVER_LIMIT = 712;
    //非法手机号
    public static final Integer TEMPLATE_MISSING_PARAMETERS = 713;
    //业务限流
    public static final Integer BUSINESS_LIMIT_CONTROL = 714;
    //JSON参数不合法，只接受字符串值
    public static final Integer INVALID_JSON_PARAM = 715;
    //黑名单管控
    public static final Integer BLACK_KEY_CONTROL_LIMIT = 716;
    //参数超出长度限制
    public static final Integer PARAM_LENGTH_LIMIT = 717;
    //不支持URL
    public static final Integer PARAM_NOT_SUPPORT_URL = 718;
    //账户余额不足
    public static final Integer AMOUNT_NOT_ENOUGH = 719;

    private Integer code;
    private String message;
    private String uid;
    private String requestId;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
