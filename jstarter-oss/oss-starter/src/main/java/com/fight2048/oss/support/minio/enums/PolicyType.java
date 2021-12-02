package com.fight2048.oss.support.minio.enums;

/**
 * <p>
 * MinIO 策略
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/7 14:53
 */
public enum PolicyType {

    /**
     * 只读
     */
    READ("read", "只读"),

    /**
     * 只写
     */
    WRITE("write", "只写"),

    /**
     * 读写
     */
    READ_WRITE("read_write", "读写");

    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String policy;

    PolicyType(String type, String policy) {
        this.type = type;
        this.policy = policy;
    }

    public String getType() {
        return type;
    }

    public String getPolicy() {
        return policy;
    }
}
