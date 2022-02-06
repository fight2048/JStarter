package com.fight2048.logging;

import com.fight2048.logging.events.LoggerAfterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@Slf4j
@SpringBootApplication
public class LoggingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoggingApplication.class, args);
    }

    @EventListener
    public void onLogger(LoggerAfterEvent event) {
        log.info("log-->{}", event);
    }
}
