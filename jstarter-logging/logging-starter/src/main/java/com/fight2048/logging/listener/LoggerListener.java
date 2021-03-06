package com.fight2048.logging.listener;

import com.fight2048.logging.LoggerMetadate;

public interface LoggerListener {

    /**
     * 当产生访问日志时,将调用此方法.注意,此方法内的操作应尽量设置为异步操作,否则可能影响请求性能
     *
     * @param metadate 产生的日志信息
     */
    void onLogger(LoggerMetadate metadate);

    default void onLogBefore(LoggerMetadate metadate) {
    }

    default void onLogAfter(LoggerMetadate metadate) {
    }
}
