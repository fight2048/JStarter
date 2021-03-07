package cn.itsite.oss;

import cn.itsite.oss.model.OssFile;
import cn.itsite.oss.model.OssMeta;
import com.qiniu.common.QiniuException;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Oss 接口
 */
public interface OssTemplate {

    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    void createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ErrorResponseException, NoResponseException, InvalidBucketNameException, XmlPullParserException, InternalException, RegionConflictException, InvalidObjectPrefixException;

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    void deleteBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean bucketExists(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    void copyFile(String sourceBucketName, String fileName, String targetBucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException;

    /**
     * 拷贝文件，重命名
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     * @param targetFileName   目标存储桶文件名称
     */
    void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

    /**
     * 获取文件元信息
     *
     * @param fileName 存储桶文件名称
     * @return 文件元信息
     */
    OssMeta getFileMetaInfo(String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException;

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶文件名称
     * @return 文件元信息
     */
    OssMeta getFileMetaInfo(String bucketName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 获取文件相对路径
     *
     * @param fileName 存储桶对象名称
     * @return 文件相对路径
     */
    String getFilePath(String fileName);

    /**
     * 获取文件相对路径
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @return 文件相对路径
     */
    String getFilePath(String bucketName, String fileName);

    /**
     * 获取文件地址
     *
     * @param fileName 存储桶对象名称
     * @return 文件地址
     */
    String getFileLink(String fileName);

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @return 文件地址
     */
    String getFileLink(String bucketName, String fileName);

    /**
     * 上传文件
     *
     * @param file 上传文件类
     * @return 文件信息
     */
    OssFile uploadFile(MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, RegionConflictException;

    /**
     * 上传文件
     *
     * @param file     上传文件类
     * @param fileName 上传文件名
     * @return 文件信息
     */
    OssFile uploadFile(String fileName, MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InternalException;

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   上传文件名
     * @param file       上传文件类
     * @return 文件信息
     */
    OssFile uploadFile(String bucketName, String fileName, MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InvalidBucketNameException, InvalidObjectPrefixException, InternalException, NoResponseException, InsufficientDataException, ErrorResponseException, RegionConflictException;

    /**
     * 上传文件
     *
     * @param fileName 存储桶对象名称
     * @param stream   文件流
     * @return 文件信息
     */
    OssFile uploadFile(String fileName, InputStream stream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InvalidBucketNameException, InvalidObjectPrefixException, InternalException, NoResponseException, InsufficientDataException, ErrorResponseException, RegionConflictException;

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    OssFile uploadFile(String bucketName, String fileName, InputStream stream) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidObjectPrefixException, InvalidKeyException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidArgumentException;

    /**
     * 删除文件
     *
     * @param fileName 存储桶对象名称
     */
    void deleteFile(String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     */
    void deleteFile(String bucketName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 批量删除文件
     *
     * @param fileNames 存储桶对象名称集合
     */
    void deleteFiles(List<String> fileNames) throws QiniuException;

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  存储桶对象名称集合
     */
    void deleteFiles(String bucketName, List<String> fileNames) throws QiniuException;
}
