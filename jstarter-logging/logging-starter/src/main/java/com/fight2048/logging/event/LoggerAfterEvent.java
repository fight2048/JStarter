package com.fight2048.logging.event;

import com.fight2048.logging.LoggerMetadate;

public class LoggerAfterEvent {
    private LoggerMetadate logger;

    public LoggerAfterEvent(LoggerMetadate logger) {
        this.logger = logger;
    }

    public LoggerMetadate getLogger() {
        return logger;
    }

    public void setLogger(LoggerMetadate logger) {
        this.logger = logger;
    }
}
