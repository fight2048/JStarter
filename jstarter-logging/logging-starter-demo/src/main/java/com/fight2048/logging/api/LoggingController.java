package com.fight2048.logging.api;

import com.fight2048.logging.annotation.Logger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "账号管理", tags = {"账号管理"})
//@Tag(name = "类上的Tag")
public class LoggingController {

//    @Tag(name = "方法上的Tag")
//    @Operation(summary = "获取全部机构信息(树结构)")
    @Logger(value = "我我我")
    @ApiOperation(value = "登录：通过code进行验证登录", notes = "https://connect.turingthink.com/?redirect_uri=http://127.0.0.1/kf12oi&appId=atcfu9ct2k8x&scope=all&response_type=code", httpMethod = "POST")
    @GetMapping("/logs")
    public Object logs() {
        return "OK";
    }
}