package cn.hutool.crypto.symmetric;

import cn.hutool.crypto.SecureUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 对称解密器接口，提供：
 * <ul>
 *     <li>从bytes解密</li>
 *     <li>从Hex(16进制)解密</li>
 *     <li>从Base64解密</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface SymmetricDecryptor {
    /**
     * 解密
     *
     * @param bytes 被解密的bytes
     * @return 解密后的bytes
     */
    byte[] decrypt(byte[] bytes);

    /**
     * 解密，针对大数据量，结束后不关闭流
     *
     * @param data    加密的字符串
     * @param out     输出流，可以是文件或网络位置
     * @param isClose 是否关闭流，包括输入和输出流
     */
    void decrypt(InputStream data, OutputStream out, boolean isClose);

    /**
     * 解密为字符串
     *
     * @param bytes   被解密的bytes
     * @param charset 解密后的charset
     * @return 解密后的String
     */
    default String decryptStr(byte[] bytes, Charset charset) {
        return new String(decrypt(bytes), charset);
    }

    /**
     * 解密为字符串，默认UTF-8编码
     *
     * @param bytes 被解密的bytes
     * @return 解密后的String
     */
    default String decryptStr(byte[] bytes) {
        return decryptStr(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 解密Hex（16进制）或Base64表示的字符串
     *
     * @param data 被解密的String，必须为16进制字符串或Base64表示形式
     * @return 解密后的bytes
     */
    default byte[] decrypt(String data) {
        return decrypt(SecureUtil.decode(data));
    }

    /**
     * 解密Hex（16进制）或Base64表示的字符串
     *
     * @param data    被解密的String
     * @param charset 解密后的charset
     * @return 解密后的String
     */
    default String decryptStr(String data, Charset charset) {
        return new String(decrypt(data), charset);
    }

    /**
     * 解密Hex（16进制）或Base64表示的字符串，默认UTF-8编码
     *
     * @param data 被解密的String
     * @return 解密后的String
     */
    default String decryptStr(String data) {
        return decryptStr(data, StandardCharsets.UTF_8);
    }

    /**
     * 解密，会关闭流
     *
     * @param data 被解密的bytes
     * @return 解密后的bytes
     */
    default byte[] decrypt(InputStream data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[1024];
        while ((read = data.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();
        return baos.toByteArray();
    }

    /**
     * 解密，不会关闭流
     *
     * @param data    被解密的InputStream
     * @param charset 解密后的charset
     * @return 解密后的String
     */
    default String decryptStr(InputStream data, Charset charset) throws IOException {
        return new String(decrypt(data), charset);
    }

    /**
     * 解密
     *
     * @param data 被解密的InputStream
     * @return 解密后的String
     */
    default String decryptStr(InputStream data) throws IOException {
        return decryptStr(data, StandardCharsets.UTF_8);
    }
}
