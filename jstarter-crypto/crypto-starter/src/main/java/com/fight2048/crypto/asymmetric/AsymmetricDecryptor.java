package com.fight2048.crypto.asymmetric;

import com.fight2048.crypto.SecureUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 非对称解密器接口，提供：
 * <ul>
 *     <li>从bytes解密</li>
 *     <li>从Hex(16进制)解密</li>
 *     <li>从Base64解密</li>
 *     <li>从BCD解密</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface AsymmetricDecryptor {

    /**
     * 解密
     *
     * @param bytes   被解密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     */
    byte[] decrypt(byte[] bytes, KeyType keyType);

    /**
     * 解密
     *
     * @param data    被解密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     */
    default byte[] decrypt(InputStream data, KeyType keyType) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read;
        byte[] buffer = new byte[1024];
        while ((read = data.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        baos.flush();
        return decrypt(baos.toByteArray(), keyType);
    }

    /**
     * 从Hex或Base64字符串解密，编码为UTF-8格式
     *
     * @param data    Hex（16进制）或Base64字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 解密后的bytes
     * @since 4.5.2
     */
    default byte[] decrypt(String data, KeyType keyType) {
        return decrypt(SecureUtil.decode(data), keyType);
    }

    /**
     * 解密为字符串，密文需为Hex（16进制）或Base64字符串
     *
     * @param data    数据，Hex（16进制）或Base64字符串
     * @param keyType 密钥类型
     * @param charset 加密前编码
     * @return 解密后的密文
     * @since 4.5.2
     */
    default String decryptStr(String data, KeyType keyType, Charset charset) {
        return new String(decrypt(data, keyType), charset);
    }

    /**
     * 解密为字符串，密文需为Hex（16进制）或Base64字符串
     *
     * @param data    数据，Hex（16进制）或Base64字符串
     * @param keyType 密钥类型
     * @return 解密后的密文
     * @since 4.5.2
     */
    default String decryptStr(String data, KeyType keyType) {
        return decryptStr(data, keyType, StandardCharsets.UTF_8);
    }
}
