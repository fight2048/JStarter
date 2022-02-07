package com.fight2048.logging.event;

import com.fight2048.logging.LoggerMetadate;

public class LoggerAfterEvent {
    private LoggerMetadate metadate;

    public LoggerAfterEvent(LoggerMetadate metadate) {
        this.metadate = metadate;
    }

    public LoggerMetadate getMetadate() {
        return metadate;
    }

    public void setMetadate(LoggerMetadate metadate) {
        this.metadate = metadate;
    }
}
