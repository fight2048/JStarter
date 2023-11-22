package com.fight2048.oss;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class KeyValue extends LinkedCaseInsensitiveMap<Object> {


    /**
     * 设置列
     *
     * @param key   属性
     * @param value 值
     * @return 本身
     */
    public KeyValue set(String key, Object value) {
        this.put(key, value);
        return this;
    }

    /**
     * 设置列，当键或值为null时忽略
     *
     * @param key   属性
     * @param value 值
     * @return 本身
     */
    public KeyValue setIgnoreNull(String key, Object value) {
        if (null != key && null != value) {
            set(key, value);
        }
        return this;
    }

    public Object getObj(String key) {
        return super.get(key);
    }

    /**
     * 获得特定类型值
     *
     * @param <T>          值类型
     * @param key          字段名
     * @param defaultValue 默认值
     * @return 字段值
     */
    public <T> T get(String key, T defaultValue) {
        final Object result = get(key);
        return (T) (result != null ? result : defaultValue);
    }

    /**
     * 获得特定类型值
     *
     * @param key 字段名
     * @return 字段值
     */
    public byte[] getBytes(String key) {
        return get(key, null);
    }

    /**
     * 获得特定类型值
     *
     * @param key 字段名
     * @return 字段值
     */
    public Date getDate(String key) {
        return get(key, null);
    }

    /**
     * 获得特定类型值
     *
     * @param key 字段名
     * @return 字段值
     */
    public Time getTime(String key) {
        return get(key, null);
    }

    /**
     * 获得特定类型值
     *
     * @param key 字段名
     * @return 字段值
     */
    public Timestamp getTimestamp(String key) {
        return get(key, null);
    }

    /**
     * 获得特定类型值
     *
     * @param key 字段名
     * @return 字段值
     */
    public Number getNumber(String key) {
        return get(key, null);
    }

    @Override
    public KeyValue clone() {
        return (KeyValue) super.clone();
    }
}