package com.fight2048.crypto.digest;

import com.fight2048.crypto.CryptoException;
import com.fight2048.crypto.SecureUtil;
import com.fight2048.crypto.utils.HexUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Objects;

/**
 * 摘要算法<br>
 * 注意：此对象实例化后为非线程安全！
 *
 * @author Looly
 */
public class Digester implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageDigest digest;
    /**
     * 盐值
     */
    protected byte[] salt;
    /**
     * 加盐位置，即将盐值字符串放置在数据的index数，默认0
     */
    protected int saltPosition;
    /**
     * 散列次数
     */
    protected int digestCount;

    // ------------------------------------------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param algorithm 算法枚举
     */
    public Digester(DigestAlgorithm algorithm) {
        this(algorithm.getValue());
    }

    /**
     * 构造
     *
     * @param algorithm 算法枚举
     */
    public Digester(String algorithm) {
        this(algorithm, null);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     * @since 4.5.1
     */
    public Digester(DigestAlgorithm algorithm, Provider provider) {
        init(algorithm.getValue(), provider);
    }

    /**
     * 构造
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     * @since 4.5.1
     */
    public Digester(String algorithm, Provider provider) {
        init(algorithm, provider);
    }
    // ------------------------------------------------------------------------------------------- Constructor end

    /**
     * 初始化
     *
     * @param algorithm 算法
     * @param provider  算法提供者，null表示JDK默认，可以引入Bouncy Castle等来提供更多算法支持
     * @return Digester
     * @throws CryptoException Cause by IOException
     */
    public Digester init(String algorithm, Provider provider) {
        if (null == provider) {
            this.digest = SecureUtil.createMessageDigest(algorithm);
        } else {
            try {
                this.digest = MessageDigest.getInstance(algorithm, provider);
            } catch (NoSuchAlgorithmException e) {
                throw new CryptoException(e);
            }
        }
        return this;
    }

    /**
     * 设置加盐内容
     *
     * @param salt 盐值
     * @return this
     * @since 4.4.3
     */
    public Digester setSalt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    /**
     * 设置加盐的位置，只有盐值存在时有效<br>
     * 加盐的位置指盐位于数据byte数组中的位置，例如：
     *
     * <pre>
     * data: 0123456
     * </pre>
     * <p>
     * 则当saltPosition = 2时，盐位于data的1和2中间，即第二个空隙，即：
     *
     * <pre>
     * data: 01[salt]23456
     * </pre>
     *
     * @param saltPosition 盐的位置
     * @return this
     * @since 4.4.3
     */
    public Digester setSaltPosition(int saltPosition) {
        this.saltPosition = saltPosition;
        return this;
    }

    /**
     * 设置重复计算摘要值次数
     *
     * @param digestCount 摘要值次数
     * @return this
     */
    public Digester setDigestCount(int digestCount) {
        this.digestCount = digestCount;
        return this;
    }

    /**
     * 重置{@link MessageDigest}
     *
     * @return this
     * @since 4.5.1
     */
    public Digester reset() {
        this.digest.reset();
        return this;
    }

    // ------------------------------------------------------------------------------------------- Digest

    /**
     * 生成文件摘要
     *
     * @param data        被摘要数据
     * @param charsetName 编码
     * @return 摘要
     */
    public byte[] digest(String data, String charsetName) {
        return digest(data,
                Objects.isNull(charsetName)
                        ? Charset.defaultCharset()
                        : Charset.forName(charsetName));
    }

    /**
     * 生成文件摘要
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     * @since 4.6.0
     */
    public byte[] digest(String data, Charset charset) {
        return digest(data.getBytes(charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public byte[] digest(String data) {
        return digest(data, StandardCharsets.UTF_8);
    }

    /**
     * 生成文件摘要，并转为16进制字符串
     *
     * @param data        被摘要数据
     * @param charsetName 编码
     * @return 摘要
     */
    public String digestHex(String data, String charsetName) {
        return digestHex(data,
                Objects.isNull(charsetName)
                        ? Charset.defaultCharset()
                        : Charset.forName(charsetName));
    }

    /**
     * 生成文件摘要，并转为16进制字符串
     *
     * @param data    被摘要数据
     * @param charset 编码
     * @return 摘要
     * @since 4.6.0
     */
    public String digestHex(String data, Charset charset) {
        return HexUtils.encodeHexStr(digest(data, charset));
    }

    /**
     * 生成文件摘要
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(String data) {
        return digestHex(data, StandardCharsets.UTF_8.name());
    }

    /**
     * 生成文件摘要<br>
     * 使用默认缓存大小
     *
     * @param file 被摘要文件
     * @return 摘要bytes
     * @throws CryptoException Cause by IOException
     */
    public byte[] digest(File file) throws CryptoException, IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            return digest(in);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    // 静默关闭
                }
            }
        }
    }

    /**
     * 生成文件摘要，并转为16进制字符串<br>
     * 使用默认缓存大小
     *
     * @param file 被摘要文件
     * @return 摘要
     */
    public String digestHex(File file) throws IOException {
        return HexUtils.encodeHexStr(digest(file));
    }

    /**
     * 生成摘要，考虑加盐和重复摘要次数
     *
     * @param data 数据bytes
     * @return 摘要bytes
     */
    public byte[] digest(byte[] data) {
        byte[] result;
        if (this.saltPosition <= 0) {
            // 加盐在开头，自动忽略空盐值
            result = doDigest(this.salt, data);
        } else if (this.saltPosition >= data.length) {
            // 加盐在末尾，自动忽略空盐值
            result = doDigest(data, this.salt);
        } else if (Objects.nonNull(salt)) {
            // 加盐在中间
            this.digest.update(data, 0, this.saltPosition);
            this.digest.update(this.salt);
            this.digest.update(data, this.saltPosition, data.length - this.saltPosition);
            result = this.digest.digest();
        } else {
            // 无加盐
            result = doDigest(data);
        }

        return resetAndRepeatDigest(result);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(byte[] data) {
        return HexUtils.encodeHexStr(digest(data));
    }

    /**
     * 生成摘要，使用默认缓存大小
     *
     * @param data {@link InputStream} 数据流
     * @return 摘要bytes
     */
    public byte[] digest(InputStream data) throws IOException {
        return digest(data, 1024);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     * 使用默认缓存大小
     *
     * @param data 被摘要数据
     * @return 摘要
     */
    public String digestHex(InputStream data) throws IOException {
        return HexUtils.encodeHexStr(digest(data));
    }

    /**
     * 生成摘要
     *
     * @param data         {@link InputStream} 数据流
     * @param bufferLength 缓存长度
     * @return 摘要bytes
     */
    public byte[] digest(InputStream data, int bufferLength) throws IOException {
        if (bufferLength < 1) {
            bufferLength = 1024;
        }

        byte[] result;

        if (Objects.isNull(salt)) {
            result = digestWithoutSalt(data, bufferLength);
        } else {
            result = digestWithSalt(data, bufferLength);
        }

        return resetAndRepeatDigest(result);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     * 使用默认缓存大小
     *
     * @param data         被摘要数据
     * @param bufferLength 缓存长度
     * @return 摘要
     */
    public String digestHex(InputStream data, int bufferLength) throws IOException {
        return HexUtils.encodeHexStr(digest(data, bufferLength));
    }

    /**
     * 获得 {@link MessageDigest}
     *
     * @return {@link MessageDigest}
     */
    public MessageDigest getDigest() {
        return digest;
    }

    /**
     * 获取散列长度，0表示不支持此方法
     *
     * @return 散列长度，0表示不支持此方法
     * @since 4.5.0
     */
    public int getDigestLength() {
        return this.digest.getDigestLength();
    }

    // -------------------------------------------------------------------------------- Private method start

    /**
     * 生成摘要
     *
     * @param data         {@link InputStream} 数据流
     * @param bufferLength 缓存长度
     * @return 摘要bytes
     * @throws IOException 从流中读取数据引发的IO异常
     */
    private byte[] digestWithoutSalt(InputStream data, int bufferLength) throws IOException {
        final byte[] buffer = new byte[bufferLength];
        int read;
        while ((read = data.read(buffer, 0, bufferLength)) > -1) {
            this.digest.update(buffer, 0, read);
        }
        return this.digest.digest();
    }

    /**
     * 生成摘要
     *
     * @param data         {@link InputStream} 数据流
     * @param bufferLength 缓存长度
     * @return 摘要bytes
     * @throws IOException 从流中读取数据引发的IO异常
     */
    private byte[] digestWithSalt(InputStream data, int bufferLength) throws IOException {
        if (this.saltPosition <= 0) {
            // 加盐在开头
            this.digest.update(this.salt);
        }

        final byte[] buffer = new byte[bufferLength];
        int total = 0;
        int read;
        while ((read = data.read(buffer, 0, bufferLength)) > -1) {
            total += read;
            if (this.saltPosition > 0 && total >= this.saltPosition) {
                if (total != this.saltPosition) {
                    digest.update(buffer, 0, total - this.saltPosition);
                }
                // 加盐在中间
                this.digest.update(this.salt);
                this.digest.update(buffer, total - this.saltPosition, read);
            } else {
                this.digest.update(buffer, 0, read);
            }
        }

        if (total < this.saltPosition) {
            // 加盐在末尾
            this.digest.update(this.salt);
        }

        return this.digest.digest();
    }

    /**
     * 生成摘要
     *
     * @param datas 数据bytes
     * @return 摘要bytes
     * @since 4.4.3
     */
    private byte[] doDigest(byte[]... datas) {
        for (byte[] data : datas) {
            if (null != data) {
                this.digest.update(data);
            }
        }
        return this.digest.digest();
    }

    /**
     * 重复计算摘要，取决于{@link #digestCount} 值<br>
     * 每次计算摘要前都会重置{@link #digest}
     *
     * @param digestData 第一次摘要过的数据
     * @return 摘要
     */
    private byte[] resetAndRepeatDigest(byte[] digestData) {
        final int digestCount = Math.max(1, this.digestCount);
        reset();
        for (int i = 0; i < digestCount - 1; i++) {
            digestData = doDigest(digestData);
            reset();
        }
        return digestData;
    }
    // -------------------------------------------------------------------------------- Private method end
}
