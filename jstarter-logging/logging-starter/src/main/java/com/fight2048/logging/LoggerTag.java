package com.fight2048.logging;

import com.fight2048.logging.annotation.Logger;

public class LoggerTag {
    private String action;

    private Logger.Type type;

    public LoggerTag(String action) {
        this.action = action;
    }

    public LoggerTag(String action, Logger.Type type) {
        this.action = action;
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Logger.Type getType() {
        return type;
    }

    public void setType(Logger.Type type) {
        this.type = type;
    }
}

