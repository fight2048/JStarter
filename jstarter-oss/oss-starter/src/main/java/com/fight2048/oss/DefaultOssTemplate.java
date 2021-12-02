package com.fight2048.oss;

import com.fight2048.oss.model.OssFile;
import com.fight2048.oss.model.OssMeta;
import com.qiniu.common.QiniuException;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DefaultOssTemplate implements OssTemplate {
    private OssTemplate template;

    public DefaultOssTemplate(OssTemplate template) {
        this.template = template;
    }

    @Override
    public void createBucket(String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidObjectPrefixException, RegionConflictException {
        template.createBucket(bucketName);
    }

    @Override
    public void deleteBucket(String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        template.deleteBucket(bucketName);
    }

    @Override
    public boolean bucketExists(String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return template.bucketExists(bucketName);
    }

    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        template.copyFile(sourceBucketName, fileName, targetBucketName);
    }

    @Override
    public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        template.copyFile(sourceBucketName, fileName, targetBucketName, targetFileName);
    }

    @Override
    public OssMeta getFileMetaInfo(String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        return template.getFileMetaInfo(fileName);
    }

    @Override
    public OssMeta getFileMetaInfo(String bucketName, String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
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
    public OssFile uploadFile(MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InternalException {
        return template.uploadFile(file);
    }

    @Override
    public OssFile uploadFile(String fileName, MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, RegionConflictException {
        return template.uploadFile(fileName, file);
    }

    @Override
    public OssFile uploadFile(String bucketName, String fileName, MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InternalException {
        return template.uploadFile(bucketName, fileName, file);
    }

    @Override
    public OssFile uploadFile(String fileName, InputStream stream) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InternalException {
        return template.uploadFile(fileName, stream);
    }

    @Override
    public OssFile uploadFile(String bucketName, String fileName, InputStream stream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InvalidBucketNameException, InvalidObjectPrefixException, InternalException, NoResponseException, InsufficientDataException, ErrorResponseException, RegionConflictException {
        return template.uploadFile(bucketName, fileName, stream);
    }

    @Override
    public void deleteFile(String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        template.deleteFile(fileName);
    }

    @Override
    public void deleteFile(String bucketName, String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        template.deleteFile(bucketName, fileName);
    }

    @Override
    public void deleteFiles(List<String> fileNames) throws QiniuException {
        template.deleteFiles(fileNames);
    }

    @Override
    public void deleteFiles(String bucketName, List<String> fileNames) throws QiniuException {
        template.deleteFiles(bucketName, fileNames);
    }
}
