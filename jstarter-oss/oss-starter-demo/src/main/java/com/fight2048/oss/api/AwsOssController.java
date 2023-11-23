//package com.fight2048.oss.api;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.fight2048.oss.autoconfigure.aws.AwsOssTemplate;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * @author: fight2048
// * @e-mail: fight2048@outlook.com
// * @version: v0.0.0
// * @blog: https://github.com/fight2048
// * @time: 2019/2/10 0010 23:51
// * @description:
// */
//@Slf4j
//@RestController
//@RequestMapping("/oss/aws")
//public class AwsOssController {
//    @Autowired
//    private AmazonS3 amazonS3;
//    @Autowired
//    private AwsOssTemplate template;
//
//    @GetMapping("/signature")
//    public Object getSignature(String key) {
//        return template.getUrl(key);
//    }
//
//    @PostMapping("/file")
//    public Object postFile(@RequestParam("file") MultipartFile file) {
//        return template.upload("test/" + file.getOriginalFilename(), file);
//    }
//
//    @DeleteMapping("/file")
//    public Object deleteFile(String key) {
//        template.deleteObject(key);
//        return "OK";
//    }
//}