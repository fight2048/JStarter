package cn.itsite.oss;

import cn.itsite.oss.model.OssFile;
import cn.itsite.oss.model.OssMeta;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public class DefaultOssTemplate implements OssTemplate {
    private OssTemplate template;

    public DefaultOssTemplate(OssTemplate template) {
        this.template = template;
    }

    @Override
    public void createBucket(String bucketName) {
        template.createBucket(bucketName);
    }

    @Override
    public void deleteBucket(String bucketName) {
        template.deleteBucket(bucketName);
    }

    @Override
    public boolean bucketExists(String bucketName) {
        return template.bucketExists(bucketName);
    }

    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName) {
        template.copyFile(sourceBucketName, fileName, targetBucketName);
    }

    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) {
        template.copyFile(sourceBucketName, fileName, targetBucketName, targetFileName);
    }

    @Override
    public OssMeta getFileMetaInfo(String fileName) {
        return template.getFileMetaInfo(fileName);
    }

    @Override
    public OssMeta getFileMetaInfo(String bucketName, String fileName) {
        return template.getFileMetaInfo(bucketName, fileName);
    }

    @Override
    public String getFilePath(String fileName) {
        return template.getFilePath(fileName);
    }

    @Override
    public String getFilePath(String bucketName, String fileName) {
        return template.getFilePath(bucketName, fileName);
    }

    @Override
    public String getFileLink(String fileName) {
        return template.getFileLink(fileName);
    }

    @Override
    public String getFileLink(String bucketName, String fileName) {
        return template.getFileLink(bucketName, fileName);
    }

    @Override
    public OssFile uploadFile(MultipartFile file) {
        return template.uploadFile(file);
    }

    @Override
    public OssFile uploadFile(String fileName, MultipartFile file) {
        return template.uploadFile(fileName, file);
    }

    @Override
    public OssFile uploadFile(String bucketName, String fileName, MultipartFile file) {
        return template.uploadFile(bucketName, fileName, file);
    }

    @Override
    public OssFile uploadFile(String fileName, InputStream stream) {
        return template.uploadFile(fileName, stream);
    }

    @Override
    public OssFile uploadFile(String bucketName, String fileName, InputStream stream) {
        return template.uploadFile(bucketName, fileName, stream);
    }

    @Override
    public void deleteFile(String fileName) {
        template.deleteFile(fileName);
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        template.deleteFile(bucketName, fileName);
    }

    @Override
    public void deleteFiles(List<String> fileNames) {
        template.deleteFiles(fileNames);
    }

    @Override
    public void deleteFiles(String bucketName, List<String> fileNames) {
        template.deleteFiles(bucketName, fileNames);
    }
}
