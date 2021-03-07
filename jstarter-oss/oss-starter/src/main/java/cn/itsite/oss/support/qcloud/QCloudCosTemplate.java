package cn.itsite.oss.support.qcloud;

import cn.itsite.oss.OssTemplate;
import cn.itsite.oss.autoconfigure.OssProperties;
import cn.itsite.oss.model.OssFile;
import cn.itsite.oss.model.OssMeta;
import cn.itsite.oss.utils.Utils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * 腾讯云 COS 操作，参考文档：https://cloud.tencent.com/document/product/436/7751
 */
public class QCloudCosTemplate implements OssTemplate {
    private final COSClient cosClient;
    private final OssProperties ossProperties;

    public QCloudCosTemplate(COSClient cosClient, OssProperties ossProperties) {
        this.cosClient = cosClient;
        this.ossProperties = ossProperties;
    }

    /**
     * 根据规则生成存储桶名称，腾讯 COS 存储桶后必须添加 appId
     *
     * @return 存储桶名称
     */
    protected String getBucketName() {
        return getBucketName()
                .concat("-")
                .concat(ossProperties.getQcloudCos()
                        .getAppId());
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            cosClient.createBucket(bucketName);
            cosClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
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
            cosClient.deleteBucket(bucketName);
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
        return cosClient.doesBucketExist(bucketName);
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
        cosClient.copyObject(sourceBucketName, fileName, targetBucketName, targetFileName);
    }

    /**
     * 获取文件元信息
     *
     * @param fileName 存储桶文件名称
     * @return 文件元信息
     */
    @Override
    public OssMeta getFileMetaInfo(String fileName) {
        return getFileMetaInfo(ossProperties.getQcloudCos()
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
        final ObjectMetadata metadata = cosClient.getObjectMetadata(bucketName, fileName);
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
     * 获取 COS 域名地址
     *
     * @return 域名地址
     */
    private String getCosEndpoint() {
        return getCosEndpoint(ossProperties.getQcloudCos()
                .getBucketName());
    }

    /**
     * 获取 COS 域名地址
     *
     * @param bucketName 存储桶名称
     * @return 域名地址
     */
    private String getCosEndpoint(String bucketName) {
        String prefix = ossProperties.getQcloudCos()
                .getHttps() ? "https://" : "http://";

        return prefix + cosClient.getClientConfig()
                .getEndpointBuilder()
                .buildGeneralApiEndpoint(bucketName);
    }

    /**
     * 获取文件相对路径
     *
     * @param fileName 存储桶对象名称
     * @return 文件相对路径
     */
    @Override
    public String getFilePath(String fileName) {
        return getCosEndpoint()
                .concat("/")
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
        return getCosEndpoint(bucketName)
                .concat("/")
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
        return getCosEndpoint()
                .concat("/")
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
        return getCosEndpoint(bucketName)
                .concat("/")
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
            cosClient.putObject(bucketName, key, stream, null);
        } else {
            PutObjectResult response = cosClient.putObject(bucketName, key, stream, null);
            int retry = 0;
            int retryCount = 5;
            // 重试 5 次
            while (Utils.isEmpty(response.getETag()) && retry < retryCount) {
                response = cosClient.putObject(bucketName, key, stream, null);
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
        cosClient.deleteObject(getBucketName(), fileName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     */
    @Override
    public void deleteFile(String bucketName, String fileName) {
        cosClient.deleteObject(bucketName, fileName);
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
