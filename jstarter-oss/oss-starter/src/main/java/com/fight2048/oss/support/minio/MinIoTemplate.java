//package com.fight2048.oss.support.minio;
//
//import com.fight2048.oss.OssTemplate;
//import com.fight2048.oss.autoconfigure.OssProperties;
//import com.fight2048.oss.model.OssFile;
//import com.fight2048.oss.model.OssMeta;
//import com.fight2048.oss.support.minio.enums.PolicyType;
//import com.fight2048.oss.utils.Utils;
//import io.minio.MinioClient;
//import io.minio.ObjectStat;
//import io.minio.errors.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
///**
// * <p>
// * MinIO 操作，参考文档：https://docs.min.io/docs/java-client-api-reference.html
// * </p>
// *
// * @author yangkai.shen
// * @date Created in 2019/12/31 17:20
// */
//public class MinIoTemplate implements OssTemplate {
//    private final MinioClient minioClient;
//    private final OssProperties ossProperties;
//
//    public MinIoTemplate(MinioClient minioClient, OssProperties ossProperties) {
//        this.minioClient = minioClient;
//        this.ossProperties = ossProperties;
//    }
//
//    /**
//     * 创建 存储桶
//     *
//     * @param bucketName 存储桶名称
//     */
//    @Override
//    public void createBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ErrorResponseException, NoResponseException, InvalidBucketNameException, XmlPullParserException, InternalException, RegionConflictException, InvalidObjectPrefixException {
//        if (!bucketExists(bucketName)) {
//            minioClient.makeBucket(bucketName);
//            minioClient.setBucketPolicy(bucketName, getPolicyType(bucketName, PolicyType.READ));
//        }
//    }
//
//    /**
//     * 删除 存储桶
//     *
//     * @param bucketName 存储桶名称
//     */
//    @Override
//    public void deleteBucket(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
//        if (bucketExists(bucketName)) {
//            minioClient.removeBucket(bucketName);
//        }
//    }
//
//    /**
//     * 存储桶是否存在
//     *
//     * @param bucketName 存储桶名称
//     * @return boolean
//     */
//    @Override
//    public boolean bucketExists(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
//        return minioClient.bucketExists(bucketName);
//    }
//
//    /**
//     * 拷贝文件
//     *
//     * @param sourceBucketName 源存储桶名称
//     * @param fileName         存储桶文件名称
//     * @param targetBucketName 目标存储桶名称
//     */
//    @Override
//    public void copyFile(String sourceBucketName, String fileName, String targetBucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
//        copyFile(sourceBucketName, fileName, targetBucketName, fileName);
//    }
//
//    /**
//     * 拷贝文件，重命名
//     *
//     * @param sourceBucketName 源存储桶名称
//     * @param fileName         存储桶文件名称
//     * @param targetBucketName 目标存储桶名称
//     * @param targetFileName   目标存储桶文件名称
//     */
//    @Override
//    public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
//        minioClient.copyObject(sourceBucketName, fileName, targetBucketName, targetFileName);
//    }
//
//    /**
//     * 获取文件元信息
//     *
//     * @param fileName 存储桶文件名称
//     * @return 文件元信息
//     */
//    @Override
//    public OssMeta getFileMetaInfo(String fileName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
//        return getFileMetaInfo(ossProperties.getMinIo()
//                .getBucketName(), fileName);
//    }
//
//    /**
//     * 获取文件元信息
//     *
//     * @param bucketName 存储桶名称
//     * @param fileName   存储桶文件名称
//     * @return 文件元信息
//     */
//    @Override
//    public OssMeta getFileMetaInfo(String bucketName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
//        final ObjectStat objectStat = minioClient.statObject(bucketName, fileName);
//        OssMeta metaInfo = new OssMeta();
//        metaInfo.setName(Utils.isBlank(objectStat.name()) ? fileName : objectStat.name());
//        metaInfo.setLink(getFileLink(metaInfo.getName()));
//        metaInfo.setHash(String.valueOf(objectStat.hashCode()));
//        metaInfo.setLength(objectStat.length());
//        metaInfo.setUploadTime(objectStat.createdTime());
//        metaInfo.setContentType(objectStat.contentType());
//        return metaInfo;
//    }
//
//    /**
//     * 获取文件相对路径
//     *
//     * @param fileName 存储桶对象名称
//     * @return 文件相对路径
//     */
//    @Override
//    public String getFilePath(String fileName) {
//        return getBucketName().concat("/")
//                .concat(fileName);
//    }
//
//    /**
//     * 获取文件相对路径
//     *
//     * @param bucketName 存储桶名称
//     * @param fileName   存储桶对象名称
//     * @return 文件相对路径
//     */
//    @Override
//    public String getFilePath(String bucketName, String fileName) {
//        return bucketName.concat("/")
//                .concat(fileName);
//    }
//
//    /**
//     * 获取文件地址
//     *
//     * @param fileName 存储桶对象名称
//     * @return 文件地址
//     */
//    @Override
//    public String getFileLink(String fileName) {
//        return ossProperties.getMinIo()
//                .getEndpoint()
//                .concat("/")
//                .concat(getBucketName())
//                .concat("/")
//                .concat(fileName);
//    }
//
//    /**
//     * 获取文件地址
//     *
//     * @param bucketName 存储桶名称
//     * @param fileName   存储桶对象名称
//     * @return 文件地址
//     */
//    @Override
//    public String getFileLink(String bucketName, String fileName) {
//        return ossProperties.getMinIo()
//                .getEndpoint()
//                .concat("/")
//                .concat(bucketName)
//                .concat("/")
//                .concat(fileName);
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param file 上传文件类
//     * @return 文件信息
//     */
//    @Override
//    public OssFile uploadFile(MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InternalException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, RegionConflictException {
//        return uploadFile(file.getOriginalFilename(), file);
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param fileName 上传文件名
//     * @param file     上传文件类
//     * @return 文件信息
//     */
//    @Override
//    public OssFile uploadFile(String fileName, MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InternalException {
//        return uploadFile(ossProperties.getMinIo()
//                .getBucketName(), fileName, file);
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param bucketName 存储桶名称
//     * @param fileName   上传文件名
//     * @param file       上传文件类
//     * @return 文件信息
//     */
//    @Override
//    public OssFile uploadFile(String bucketName, String fileName, MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InvalidBucketNameException, InvalidObjectPrefixException, InternalException, NoResponseException, InsufficientDataException, ErrorResponseException, RegionConflictException {
//        return uploadFile(bucketName, fileName, file.getInputStream());
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param fileName 存储桶对象名称
//     * @param stream   文件流
//     * @return 文件信息
//     */
//    @Override
//    public OssFile uploadFile(String fileName, InputStream stream) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, InvalidBucketNameException, InvalidObjectPrefixException, InternalException, NoResponseException, InsufficientDataException, ErrorResponseException, RegionConflictException {
//        return uploadFile(ossProperties.getMinIo()
//                .getBucketName(), fileName, stream);
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param bucketName 存储桶名称
//     * @param fileName   存储桶对象名称
//     * @param stream     文件流
//     * @return 文件信息
//     */
//    @Override
//    public OssFile uploadFile(String bucketName, String fileName, InputStream stream) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidObjectPrefixException, InvalidKeyException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidArgumentException {
//        // 创建存储桶
//        createBucket(bucketName);
//        // 获取 oss 存储文件名
//        String key = getFileName(fileName);
//
//        minioClient.putObject(bucketName,
//                key,
//                stream,
//                (long) stream.available(),
//                null,
//                null,
//                Utils.mimeType(key));
//
//        OssFile ossFile = new OssFile();
//        ossFile.setName(key);
//        ossFile.setOriginalName(fileName);
//        ossFile.setLink(getFileLink(bucketName, key));
//        return ossFile;
//    }
//
//    /**
//     * 删除文件
//     *
//     * @param fileName 存储桶对象名称
//     */
//    @Override
//    public void deleteFile(String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
//        minioClient.removeObject(getBucketName(), fileName);
//    }
//
//    /**
//     * 删除文件
//     *
//     * @param bucketName 存储桶名称
//     * @param fileName   存储桶对象名称
//     */
//    @Override
//    public void deleteFile(String bucketName, String fileName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
//        minioClient.removeObject(bucketName, fileName);
//    }
//
//    /**
//     * 批量删除文件
//     *
//     * @param fileNames 存储桶对象名称集合
//     */
//    @Override
//    public void deleteFiles(List<String> fileNames) {
//        minioClient.removeObjects(getBucketName(), fileNames);
//    }
//
//    /**
//     * 批量删除文件
//     *
//     * @param bucketName 存储桶名称
//     * @param fileNames  存储桶对象名称集合
//     */
//    @Override
//    public void deleteFiles(String bucketName, List<String> fileNames) {
//        minioClient.removeObjects(bucketName, fileNames);
//    }
//
//    /**
//     * 获取存储桶策略
//     *
//     * @param policyType 策略枚举
//     * @return 存储桶策略
//     */
//    public String getPolicyType(PolicyType policyType) {
//        return getPolicyType(getBucketName(), policyType);
//    }
//
//    /**
//     * 获取存储桶策略，参考 {@link MinioClient#setBucketPolicy(java.lang.String, java.lang.String)} 的注释
//     *
//     * @param bucketName 存储桶名称
//     * @param policyType 策略枚举
//     * @return 存储桶策略
//     */
//    public static String getPolicyType(String bucketName, PolicyType policyType) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("{\n");
//        builder.append("    \"Statement\": [\n");
//        builder.append("        {\n");
//        builder.append("            \"Action\": [\n");
//
//        switch (policyType) {
//            case WRITE:
//                builder.append("                \"s3:GetBucketLocation\",\n");
//                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
//                break;
//            case READ_WRITE:
//                builder.append("                \"s3:GetBucketLocation\",\n");
//                builder.append("                \"s3:ListBucket\",\n");
//                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
//                break;
//            default:
//                builder.append("                \"s3:GetBucketLocation\"\n");
//                break;
//        }
//
//        builder.append("            ],\n");
//        builder.append("            \"Effect\": \"Allow\",\n");
//        builder.append("            \"Principal\": \"*\",\n");
//        builder.append("            \"Resource\": \"arn:aws:s3:::");
//        builder.append(bucketName);
//        builder.append("\"\n");
//        builder.append("        },\n");
//        if (PolicyType.READ.equals(policyType)) {
//            builder.append("        {\n");
//            builder.append("            \"Action\": [\n");
//            builder.append("                \"s3:ListBucket\"\n");
//            builder.append("            ],\n");
//            builder.append("            \"Effect\": \"Deny\",\n");
//            builder.append("            \"Principal\": \"*\",\n");
//            builder.append("            \"Resource\": \"arn:aws:s3:::");
//            builder.append(bucketName);
//            builder.append("\"\n");
//            builder.append("        },\n");
//        }
//        builder.append("        {\n");
//        builder.append("            \"Action\": ");
//
//        switch (policyType) {
//            case WRITE:
//                builder.append("[\n");
//                builder.append("                \"s3:AbortMultipartUpload\",\n");
//                builder.append("                \"s3:DeleteObject\",\n");
//                builder.append("                \"s3:ListMultipartUploadParts\",\n");
//                builder.append("                \"s3:PutObject\"\n");
//                builder.append("            ],\n");
//                break;
//            case READ_WRITE:
//                builder.append("[\n");
//                builder.append("                \"s3:AbortMultipartUpload\",\n");
//                builder.append("                \"s3:DeleteObject\",\n");
//                builder.append("                \"s3:GetObject\",\n");
//                builder.append("                \"s3:ListMultipartUploadParts\",\n");
//                builder.append("                \"s3:PutObject\"\n");
//                builder.append("            ],\n");
//                break;
//            default:
//                builder.append("\"s3:GetObject\",\n");
//                break;
//        }
//
//        builder.append("            \"Effect\": \"Allow\",\n");
//        builder.append("            \"Principal\": \"*\",\n");
//        builder.append("            \"Resource\": \"arn:aws:s3:::");
//        builder.append(bucketName);
//        builder.append("/*\"\n");
//        builder.append("        }\n");
//        builder.append("    ],\n");
//        builder.append("    \"Version\": \"2012-10-17\"\n");
//        builder.append("}\n");
//        return builder.toString();
//    }
//
//    /**
//     * 生成存储桶名称规则，默认传入的存储桶名称
//     *
//     * @return 存储桶名称
//     */
//    private String getBucketName() {
//        return ossProperties.getMinIo().getBucketName();
//    }
//
//    /**
//     * 生成文件名规则，默认 "upload/2019-12-31/5e9ec298963a4eef8c59d379d02e8a70.png"
//     *
//     * @param originalFilename 文件名
//     * @return 文件名
//     */
//    private String getFileName(String originalFilename) {
//        return "upload/" + LocalDate.now() + "/" + UUID.randomUUID().toString().replace("-", "") + "." + Utils.fileExt(originalFilename);
//    }
//}
