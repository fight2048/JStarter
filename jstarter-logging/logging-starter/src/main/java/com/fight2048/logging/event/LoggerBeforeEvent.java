package com.fight2048.logging.event;

import com.fight2048.logging.LoggerMetadate;

public class LoggerBeforeEvent {
    private LoggerMetadate logger;

    public LoggerBeforeEvent(LoggerMetadate logger) {
        this.logger = logger;
    }

    public LoggerMetadate getLogger() {
        return logger;
    }

    public void setLogger(LoggerMetadate logger) {
        this.logger = logger;
    }
}
