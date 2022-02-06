package com.fight2048.logging.api;

import com.fight2048.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @version: v0.0.0
 * @blog: https://github.com/fight2048
 * @time: 2019/10/24/0024 0:25
 * @description:
 */
@RestController
public class LoggingController {

    @Logger(value = "我我我")
    @GetMapping("/logs")
    public Object logs() {
        return "OK";
    }
}