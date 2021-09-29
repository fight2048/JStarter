package cn.itsite.oss.support.aliyun;

import cn.itsite.oss.OssTemplate;
import cn.itsite.oss.autoconfigure.OssProperties;
import cn.itsite.oss.model.OssFile;
import cn.itsite.oss.model.OssMeta;
import cn.itsite.oss.utils.Utils;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

/**
 * 阿里云 OSS 操作，参考文档：https://help.aliyun.com/document_detail/31947.html
 */
public class AliyunOssTemplate implements OssTemplate {
    private final OSSClient ossClient;
    private final OssProperties ossProperties;

    public AliyunOssTemplate(OSSClient ossClient, OssProperties ossProperties) {
        this.ossClient = ossClient;
        this.ossProperties = ossProperties;
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            ossClient.createBucket(bucketName);
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
    }

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void deleteBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            ossClient.deleteBucket(bucketName);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    @Override
    public boolean bucketExists(String bucketName) {
        return ossClient.doesBucketExist(bucketName);
    }

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName) {
        copyFile(sourceBucketName, fileName, targetBucketName, fileName);
    }

    /**
     * 拷贝文件，重命名
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     * @param targetFileName   目标存储桶文件名称
     */
    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) {
        ossClient.copyObject(sourceBucketName, fileName, targetBucketName, targetFileName);
    }

    /**
     * 获取文件元信息
     *
     * @param fileName 存储桶文件名称
     * @return 文件元信息
     */
    @Override
    public OssMeta getFileMetaInfo(String fileName) {
        return getFileMetaInfo(ossProperties.getAliyunOss()
                .getBucketName(), fileName);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶文件名称
     * @return 文件元信息
     */
    @Override
    public OssMeta getFileMetaInfo(String bucketName, String fileName) {
        final ObjectMetadata metadata = ossClient.getObjectMetadata(bucketName, fileName);
        OssMeta metaInfo = new OssMeta();
        metaInfo.setName(fileName);
        metaInfo.setLink(getFileLink(metaInfo.getName()));
        metaInfo.setHash(metadata.getContentMD5());
        metaInfo.setLength(metadata.getContentLength());
        metaInfo.setUploadTime(metadata.getLastModified());
        metaInfo.setContentType(metadata.getContentType());
        return metaInfo;
    }

    /**
     * 获取 OSS 域名地址
     *
     * @return 域名地址
     */
    private String getOssEndpoint() {
        return getOssEndpoint(ossProperties.getAliyunOss()
                .getBucketName());
    }

    /**
     * 获取 OSS 域名地址
     *
     * @param bucketName 存储桶名称
     * @return 域名地址
     */
    private String getOssEndpoint(String bucketName) {
        String prefix = ossProperties.getAliyunOss()
                .getHttps() ? "https://" : "http://";

        return prefix + bucketName + "." + ossProperties.getAliyunOss().getEndpoint()
                .replaceFirst(prefix, "");
    }

    /**
     * 获取文件相对路径
     *
     * @param fileName 存储桶对象名称
     * @return 文件相对路径
     */
    @Override
    public String getFilePath(String fileName) {
        return getOssEndpoint().concat("/")
                .concat(fileName);
    }

    /**
     * 获取文件相对路径
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @return 文件相对路径
     */
    @Override
    public String getFilePath(String bucketName, String fileName) {
        return getOssEndpoint(bucketName).concat("/")
                .concat(fileName);
    }

    /**
     * 获取文件地址
     *
     * @param fileName 存储桶对象名称
     * @return 文件地址
     */
    @Override
    public String getFileLink(String fileName) {
        return getOssEndpoint().concat("/")
                .concat(fileName);
    }

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @return 文件地址
     */
    @Override
    public String getFileLink(String bucketName, String fileName) {
        return getOssEndpoint(bucketName).concat("/")
                .concat(fileName);
    }

    /**
     * 上传文件
     *
     * @param file 上传文件类
     * @return 文件信息
     */
    @Override
    public OssFile uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file.getOriginalFilename(), file);
    }

    /**
     * 上传文件
     *
     * @param fileName 上传文件名
     * @param file     上传文件类
     * @return 文件信息
     */
    @Override
    public OssFile uploadFile(String fileName, MultipartFile file) throws IOException {
        return uploadFile(ossProperties.getAliyunOss()
                .getBucketName(), fileName, file);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   上传文件名
     * @param file       上传文件类
     * @return 文件信息
     */
    @Override
    public OssFile uploadFile(String bucketName, String fileName, MultipartFile file) throws IOException {
        return uploadFile(bucketName, fileName, file.getInputStream());
    }

    /**
     * 上传文件
     *
     * @param fileName 存储桶对象名称
     * @param stream   文件流
     * @return 文件信息
     */
    @Override
    public OssFile uploadFile(String fileName, InputStream stream) {
        return uploadFile(ossProperties.getAliyunOss()
                .getBucketName(), fileName, stream);
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    @Override
    public OssFile uploadFile(String bucketName, String fileName, InputStream stream) {
        return upload(bucketName, fileName, stream, false);
    }

    private OssFile upload(String bucketName, String fileName, InputStream stream, boolean cover) {
        // 创建存储桶
        createBucket(bucketName);
        // 获取 oss 存储文件名
        String key = getFileName(fileName);

        // 是否覆盖上传
        if (cover) {
            ossClient.putObject(bucketName, key, stream);
        } else {
            PutObjectResult response = ossClient.putObject(bucketName, key, stream);
            int retry = 0;
            int retryCount = 5;
            // 重试 5 次
            while (Utils.isEmpty(response.getETag()) && retry < retryCount) {
                response = ossClient.putObject(bucketName, key, stream);
                retry++;
            }
        }

        OssFile ossFile = new OssFile();
        ossFile.setName(key);
        ossFile.setOriginalName(fileName);
        ossFile.setLink(getFileLink(bucketName, key));
        return ossFile;
    }

    /**
     * 删除文件
     *
     * @param fileName 存储桶对象名称
     */
    @Override
    public void deleteFile(String fileName) {
        ossClient.deleteObject(getBucketName(), fileName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     */
    @Override
    public void deleteFile(String bucketName, String fileName) {
        ossClient.deleteObject(bucketName, fileName);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 存储桶对象名称集合
     */
    @Override
    public void deleteFiles(List<String> fileNames) {
        fileNames.forEach(this::deleteFile);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  存储桶对象名称集合
     */
    @Override
    public void deleteFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach(fileName -> deleteFile(bucketName, fileName));
    }

    public Map<String, String> getUploadToken() {
        return getUploadToken(ossProperties.getAliyunOss()
                .getBucketName());
    }

    /**
     * 获取上传凭证，普通上传，默认 1 小时过期
     *
     * @param bucketName 存储桶名称
     * @return 上传凭证
     */
    public Map<String, String> getUploadToken(String bucketName) {
        // 默认过期时间1小时，单位 秒
        return getUploadToken(bucketName, ossProperties.getAliyunOss()
                .getMap()
                .get("expireTime", 60 * 60));
    }

    /**
     * 获取上传凭证，普通上传
     * TODO 上传大小限制、基础路径
     *
     * @param bucketName 存储桶名称
     * @param expireTime 过期时间，单位秒
     * @return 上传凭证
     */
    public Map<String, String> getUploadToken(String bucketName, long expireTime) {
        String baseDir = LocalDate.now().toString();

        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);

        PolicyConditions policy = new PolicyConditions();
        // 默认大小限制10M
        OssProperties.AliyunOssProperties properties = ossProperties.getAliyunOss();
        long contentLengthRange = 1024 * 1024 * 10;
        if (Objects.nonNull(properties)) {
            contentLengthRange = properties.getMap().get("contentLengthRange", contentLengthRange);
        }
        policy.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, contentLengthRange);
        policy.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, baseDir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policy);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> map = new LinkedHashMap<>(16);
        map.put("accessid", ossProperties.getAliyunOss()
                .getAccessKey());
        map.put("policy", encodedPolicy);
        map.put("signature", postSignature);
        map.put("dir", baseDir);
        map.put("host", getOssEndpoint(bucketName));
        map.put("expire", String.valueOf(expireEndTime / 1000));
        return map;
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    private String getBucketName() {
        return ossProperties.getAliyunOss().getBucketName();
    }

    /**
     * 生成文件名规则，默认 "upload/2019-12-31/5e9ec298963a4eef8c59d379d02e8a70.png"
     *
     * @param originalFilename 文件名
     * @return 文件名
     */
    private String getFileName(String originalFilename) {
        return "upload/" + LocalDate.now() + "/" + UUID.randomUUID().toString().replace("-", "") + "." + Utils.fileExt(originalFilename);
    }
}
