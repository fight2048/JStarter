package cn.itsite.oss.api;

import cn.itsite.oss.DefaultOssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2019/10/24/0024 0:25
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/files")
public class OssController {

    @Autowired
    DefaultOssTemplate defaultOssTemplate;

    @GetMapping("/images")
    public Object getImage() {
        defaultOssTemplate.bucketExists("111");
        return "";

    }

    @PostMapping("/images")
    public Object postImage(@RequestParam("file") MultipartFile file) {
        return "";
    }
}