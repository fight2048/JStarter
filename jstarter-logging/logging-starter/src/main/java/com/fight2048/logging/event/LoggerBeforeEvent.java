package com.fight2048.logging.event;

import com.fight2048.logging.LoggerMetadate;

public class LoggerBeforeEvent {
    private LoggerMetadate metadate;

    public LoggerBeforeEvent(LoggerMetadate metadate) {
        this.metadate = metadate;
    }

    public LoggerMetadate getMetadate() {
        return metadate;
    }

    public void setMetadate(LoggerMetadate metadate) {
        this.metadate = metadate;
    }
}
