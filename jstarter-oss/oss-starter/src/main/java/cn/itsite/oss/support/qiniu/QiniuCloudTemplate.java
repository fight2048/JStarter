package cn.itsite.oss.support.qiniu;

import cn.itsite.oss.OssTemplate;
import cn.itsite.oss.autoconfigure.OssProperties;
import cn.itsite.oss.model.OssFile;
import cn.itsite.oss.model.OssMeta;
import cn.itsite.oss.utils.Utils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.AclType;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 七牛云操作，参考文档：https://developer.qiniu.com/kodo
 */
public class QiniuCloudTemplate implements OssTemplate {
    private final Auth auth;
    private final UploadManager uploadManager;
    private final BucketManager bucketManager;
    private final OssProperties ossProperties;

    public QiniuCloudTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, OssProperties ossProperties) {
        this.auth = auth;
        this.uploadManager = uploadManager;
        this.bucketManager = bucketManager;
        this.ossProperties = ossProperties;
    }

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void createBucket(String bucketName) throws QiniuException {
        if (!bucketExists(bucketName)) {
            bucketManager.createBucket(bucketName, ossProperties.getQiniuCloud()
                    .getRegion());
            bucketManager.putBucketAccessMode(bucketName, AclType.PUBLIC);
        }
    }

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void deleteBucket(String bucketName) {
        // TODO: 七牛云 Java SDK 暂未提供该接口
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    @Override
    public boolean bucketExists(String bucketName) throws QiniuException {
        final String[] buckets = bucketManager.buckets();
        return Utils.contains(buckets, bucketName);
    }

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName) throws QiniuException {
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
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) throws QiniuException {
        bucketManager.copy(sourceBucketName, fileName, targetBucketName, targetFileName);
    }

    /**
     * 获取文件元信息
     *
     * @param fileName 存储桶文件名称
     * @return 文件元信息
     */
    @Override
    public OssMeta getFileMetaInfo(String fileName) throws QiniuException {
        return getFileMetaInfo(ossProperties.getQiniuCloud().getBucketName(), fileName);
    }

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶文件名称
     * @return 文件元信息
     */
    @Override
    public OssMeta getFileMetaInfo(String bucketName, String fileName) throws QiniuException {
        final FileInfo fileInfo = bucketManager.stat(bucketName, fileName);
        OssMeta metaInfo = new OssMeta();
        metaInfo.setName(Utils.isBlank(fileInfo.key) ? fileName : fileInfo.key);
        metaInfo.setLink(getFileLink(metaInfo.getName()));
        metaInfo.setHash(fileInfo.hash);
        metaInfo.setLength(fileInfo.fsize);
        // 单位是 100 纳秒 所以除以 1000 * 10
        metaInfo.setUploadTime(new Date(fileInfo.putTime / (1000 * 10)));
        metaInfo.setContentType(fileInfo.mimeType);
        return metaInfo;
    }

    /**
     * 获取文件相对路径
     *
     * @param fileName 存储桶对象名称
     * @return 文件相对路径
     */
    @Override
    public String getFilePath(String fileName) {
        return getBucketName().concat("/")
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
        return bucketName.concat("/")
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
        return ossProperties.getQiniuCloud()
                .getEndpoint()
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
        return ossProperties.getQiniuCloud()
                .getEndpoint()
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
        return uploadFile(ossProperties.getQiniuCloud()
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
    public OssFile uploadFile(String fileName, InputStream stream) throws QiniuException {
        return uploadFile(ossProperties.getQiniuCloud()
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
    public OssFile uploadFile(String bucketName, String fileName, InputStream stream) throws QiniuException {
        return upload(bucketName, fileName, stream, false);
    }

    private OssFile upload(String bucketName, String fileName, InputStream stream, boolean cover) throws QiniuException {
        // 创建存储桶
        createBucket(bucketName);
        // 获取 oss 存储文件名
        String key = getFileName(fileName);

        // 是否覆盖上传
        if (cover) {
            uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
        } else {
            Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
            int retry = 0;
            int retryCount = 5;
            while (response.needRetry() && retry < retryCount) {
                response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
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
    public void deleteFile(String fileName) throws QiniuException {
        bucketManager.delete(getBucketName(), fileName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     */
    @Override
    public void deleteFile(String bucketName, String fileName) throws QiniuException {
        bucketManager.delete(bucketName, fileName);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 存储桶对象名称集合
     */
    @Override
    public void deleteFiles(List<String> fileNames) throws QiniuException{
        fileNames.forEach(fileName -> {
            try {
                deleteFile(fileName);
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  存储桶对象名称集合
     */
    @Override
    public void deleteFiles(String bucketName, List<String> fileNames) throws QiniuException{
        fileNames.forEach(fileName -> {
            try {
                deleteFile(bucketName, fileName);
            } catch (QiniuException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取上传凭证，普通上传
     *
     * @param bucketName 存储桶名称
     * @return Token
     */
    private String getUploadToken(String bucketName) {
        return auth.uploadToken(bucketName);
    }

    /**
     * 获取上传凭证，覆盖上传
     *
     * @param bucketName 存储桶名称
     * @param key        文件名
     * @return Token
     */
    private String getUploadToken(String bucketName, String key) {
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 生成存储桶名称规则，默认传入的存储桶名称
     *
     * @return 存储桶名称
     */
    private String getBucketName() {
        return ossProperties.getQiniuCloud().getBucketName();
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
