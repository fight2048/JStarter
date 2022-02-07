package com.fight2048.logging.event;

import com.fight2048.logging.LoggerMetadate;

public class LoggerAfterEvent {
    private LoggerMetadate logger;

    public LoggerAfterEvent(LoggerMetadate metadate) {
        this.logger = metadate;
    }

    public LoggerMetadate getMetadate() {
        return logger;
    }

    public void setMetadate(LoggerMetadate metadate) {
        this.logger = metadate;
    }
}
