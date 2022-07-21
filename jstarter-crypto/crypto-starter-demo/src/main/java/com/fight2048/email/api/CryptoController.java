package com.fight2048.email.api;

import com.fight2048.crypto.KeyUtil;
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
public class CryptoController {

    @GetMapping("/crypto")
    public Object crypto() {

//        KeyUtil.generateKey();

        return "success";
    }
}