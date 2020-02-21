package cn.itsite.oss;

import cn.itsite.oss.model.OssFile;
import cn.itsite.oss.model.OssMeta;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
    void createBucket(String bucketName);

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    void deleteBucket(String bucketName);

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean bucketExists(String bucketName);

    /**
     * 拷贝文件
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     */
    void copyFile(String sourceBucketName, String fileName, String targetBucketName);

    /**
     * 拷贝文件，重命名
     *
     * @param sourceBucketName 源存储桶名称
     * @param fileName         存储桶文件名称
     * @param targetBucketName 目标存储桶名称
     * @param targetFileName   目标存储桶文件名称
     */
    void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName);

    /**
     * 获取文件元信息
     *
     * @param fileName 存储桶文件名称
     * @return 文件元信息
     */
    OssMeta getFileMetaInfo(String fileName);

    /**
     * 获取文件元信息
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶文件名称
     * @return 文件元信息
     */
    OssMeta getFileMetaInfo(String bucketName, String fileName);

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
    OssFile uploadFile(MultipartFile file);

    /**
     * 上传文件
     *
     * @param file     上传文件类
     * @param fileName 上传文件名
     * @return 文件信息
     */
    OssFile uploadFile(String fileName, MultipartFile file);

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   上传文件名
     * @param file       上传文件类
     * @return 文件信息
     */
    OssFile uploadFile(String bucketName, String fileName, MultipartFile file);

    /**
     * 上传文件
     *
     * @param fileName 存储桶对象名称
     * @param stream   文件流
     * @return 文件信息
     */
    OssFile uploadFile(String fileName, InputStream stream);

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @param stream     文件流
     * @return 文件信息
     */
    OssFile uploadFile(String bucketName, String fileName, InputStream stream);

    /**
     * 删除文件
     *
     * @param fileName 存储桶对象名称
     */
    void deleteFile(String fileName);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     */
    void deleteFile(String bucketName, String fileName);

    /**
     * 批量删除文件
     *
     * @param fileNames 存储桶对象名称集合
     */
    void deleteFiles(List<String> fileNames);

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  存储桶对象名称集合
     */
    void deleteFiles(String bucketName, List<String> fileNames);
}
