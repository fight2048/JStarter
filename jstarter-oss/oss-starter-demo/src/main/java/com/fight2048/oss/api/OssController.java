package com.fight2048.oss.api;

import com.aliyun.oss.OSS;
import com.fight2048.oss.autoconfigure.aliyun.AliyunOssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @version: v0.0.0
 * @blog: https://github.com/fight2048
 * @time: 2019/2/10 0010 23:51
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private AliyunOssTemplate aliyunOssTemplate;
    @Autowired
    private OSS oss;

    @GetMapping("/signature")
    public Object getSign() {
        return aliyunOssTemplate.getUploadToken("telemedicine-files", 111 + "");
    }

    @PostMapping("/file")
    public Object postFile(@RequestParam("file") MultipartFile file) throws IOException {
        return oss.putObject("telemedicine-files", "file56565", file.getInputStream());
    }

    @DeleteMapping("/file")
    public Object deleteFile(String key) {
        oss.deleteObject("telemedicine-files", 111 + "");
        return "OK";
    }
}