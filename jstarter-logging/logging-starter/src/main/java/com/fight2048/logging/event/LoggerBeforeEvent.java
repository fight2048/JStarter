package com.fight2048.logging.event;

import com.fight2048.logging.LoggerMetadate;

public class LoggerBeforeEvent {
    private LoggerMetadate logger;

    public LoggerBeforeEvent(LoggerMetadate metadate) {
        this.logger = metadate;
    }

    public LoggerMetadate getMetadate() {
        return logger;
    }

    public void setMetadate(LoggerMetadate metadate) {
        this.logger = metadate;
    }
}
