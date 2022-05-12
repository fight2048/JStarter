package com.fight2048.crypto.asymmetric;

import com.fight2048.crypto.utils.HexUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 非对称加密器接口，提供：
 * <ul>
 *     <li>加密为bytes</li>
 *     <li>加密为Hex(16进制)</li>
 *     <li>加密为Base64</li>
 *     <li>加密为BCD</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.12
 */
public interface AsymmetricEncryptor {

    /**
     * 加密
     *
     * @param data    被加密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    byte[] encrypt(byte[] data, KeyType keyType);

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     */
    default String encryptHex(byte[] data, KeyType keyType) {
        return HexUtils.encodeHexStr(encrypt(data, keyType));
    }

    /**
     * 编码为Base64字符串
     *
     * @param data    被加密的bytes
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    default String encryptBase64(byte[] data, KeyType keyType) {
        return Base64.getEncoder().encodeToString(encrypt(data, keyType));
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    default byte[] encrypt(String data, String charset, KeyType keyType) throws UnsupportedEncodingException {
        return encrypt(data.getBytes(charset), keyType);
    }

    /**
     * 加密
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    default byte[] encrypt(String data, Charset charset, KeyType keyType) {
        return encrypt(data.getBytes(charset), keyType);
    }

    /**
     * 加密，使用UTF-8编码
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return 加密后的bytes
     */
    default byte[] encrypt(String data, KeyType keyType) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), keyType);
    }

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     * @since 4.0.1
     */
    default String encryptHex(String data, KeyType keyType) {
        return HexUtils.encodeHexStr(encrypt(data, keyType));
    }

    /**
     * 编码为Hex字符串
     *
     * @param data    被加密的bytes
     * @param charset 编码
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Hex字符串
     * @since 4.0.1
     */
    default String encryptHex(String data, Charset charset, KeyType keyType) {
        return HexUtils.encodeHexStr(encrypt(data, charset, keyType));
    }

    /**
     * 编码为Base64字符串，使用UTF-8编码
     *
     * @param data    被加密的字符串
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    default String encryptBase64(String data, KeyType keyType) {
        return Base64.getEncoder().encodeToString(encrypt(data, keyType));
    }

    /**
     * 编码为Base64字符串
     *
     * @param data    被加密的字符串
     * @param charset 编码
     * @param keyType 私钥或公钥 {@link KeyType}
     * @return Base64字符串
     * @since 4.0.1
     */
    default String encryptBase64(String data, Charset charset, KeyType keyType) {
        return Base64.getEncoder().encodeToString(encrypt(data, charset, keyType));
    }
}
