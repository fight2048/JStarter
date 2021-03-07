package cn.itsite.oss.api;

import cn.itsite.oss.DefaultOssTemplate;
import cn.itsite.oss.model.OssFile;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    public Object getImage() throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        defaultOssTemplate.bucketExists("111");
        return "";
    }

    @PostMapping("/images")
    public Object postImage(@RequestParam("file") MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, RegionConflictException {
        OssFile ossFile = defaultOssTemplate.uploadFile(file);
        return ossFile.getLink();
    }

    @DeleteMapping("/images")
    public Object postImage(String key) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        log.info("postImage");
        defaultOssTemplate.deleteFile(key);
        return "";
    }
}