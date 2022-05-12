package com.fight2048.crypto.symmetric;

import com.fight2048.crypto.utils.HexUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 对称加密器接口，提供：
 * <ul>
 *     <li>加密为bytes</li>
 *     <li>加密为Hex(16进制)</li>
 *     <li>加密为Base64</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface SymmetricEncryptor {

    /**
     * 加密
     *
     * @param data 被加密的bytes
     * @return 加密后的bytes
     */
    byte[] encrypt(byte[] data);

    /**
     * 加密，针对大数据量，可选结束后是否关闭流
     *
     * @param data    被加密的字符串
     * @param out     输出流，可以是文件或网络位置
     * @param isClose 是否关闭流
     */
    void encrypt(InputStream data, OutputStream out, boolean isClose);

    /**
     * 加密
     *
     * @param data 数据
     * @return 加密后的Hex
     */
    default String encryptHex(byte[] data) {
        return HexUtils.encodeHexStr(encrypt(data));
    }

    /**
     * 加密
     *
     * @param data 数据
     * @return 加密后的Base64
     */
    default String encryptBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的bytes
     */
    default byte[] encrypt(String data, String charset) throws UnsupportedEncodingException {
        return encrypt(data.getBytes(charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的bytes
     */
    default byte[] encrypt(String data, Charset charset) {
        return encrypt(data.getBytes(charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的Hex
     */
    default String encryptHex(String data, String charset) throws UnsupportedEncodingException {
        return HexUtils.encodeHexStr(encrypt(data, charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的Hex
     */
    default String encryptHex(String data, Charset charset) {
        return HexUtils.encodeHexStr(encrypt(data, charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的Base64
     */
    default String encryptBase64(String data, String charset) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(encrypt(data, charset));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @return 加密后的Base64
     * @since 4.5.12
     */
    default String encryptBase64(String data, Charset charset) {
        return Base64.getEncoder().encodeToString(encrypt(data, charset));
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的bytes
     */
    default byte[] encrypt(String data) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的Hex
     */
    default String encryptHex(String data) {
        return HexUtils.encodeHexStr(encrypt(data));
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data 被加密的字符串
     * @return 加密后的Base64
     */
    default String encryptBase64(String data) {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }

    /**
     * 加密，加密后关闭流
     *
     * @param data 被加密的字符串
     * @return 加密后的bytes
     */
    default byte[] encrypt(InputStream data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[1024];
        while ((read = data.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();
        return encrypt(baos.toByteArray());
    }

    /**
     * 加密
     *
     * @param data 被加密的字符串
     * @return 加密后的Hex
     */
    default String encryptHex(InputStream data) throws IOException {
        return HexUtils.encodeHexStr(encrypt(data));
    }

    /**
     * 加密
     *
     * @param data 被加密的字符串
     * @return 加密后的Base64
     */
    default String encryptBase64(InputStream data) throws IOException {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }
}
