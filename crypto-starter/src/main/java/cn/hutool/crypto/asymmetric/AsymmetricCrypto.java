package cn.hutool.crypto.asymmetric;

import cn.hutool.crypto.CipherWrapper;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 * 非对称加密算法
 *
 * <pre>
 * 1、签名：使用私钥加密，公钥解密。
 * 用于让所有公钥所有者验证私钥所有者的身份并且用来防止私钥所有者发布的内容被篡改，但是不用来保证内容不被他人获得。
 *
 * 2、加密：用公钥加密，私钥解密。
 * 用于向公钥所有者发布信息,这个信息可能被他人篡改,但是无法被他人获得。
 * </pre>
 *
 * @author Looly
 */
public class AsymmetricCrypto extends AbstractAsymmetricCrypto<AsymmetricCrypto> {
    private static final long serialVersionUID = 1L;

    /**
     * Cipher负责完成加密或解密工作
     */
    protected CipherWrapper cipherWrapper;

    /**
     * 加密的块大小
     */
    protected int encryptBlockSize = -1;
    /**
     * 解密的块大小
     */
    protected int decryptBlockSize = -1;
    // ------------------------------------------------------------------ Constructor start

    /**
     * 构造，创建新的私钥公钥对
     *
     * @param algorithm {@link SymmetricAlgorithm}
     */
    @SuppressWarnings("RedundantCast")
    public AsymmetricCrypto(AsymmetricAlgorithm algorithm) {
        this(algorithm, (byte[]) null, (byte[]) null);
    }

    /**
     * 构造，创建新的私钥公钥对
     *
     * @param algorithm 算法
     */
    @SuppressWarnings("RedundantCast")
    public AsymmetricCrypto(String algorithm) {
        this(algorithm, (byte[]) null, (byte[]) null);
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm     {@link SymmetricAlgorithm}
     * @param privateKeyStr 私钥Hex或Base64表示
     * @param publicKeyStr  公钥Hex或Base64表示
     */
    public AsymmetricCrypto(AsymmetricAlgorithm algorithm, String privateKeyStr, String publicKeyStr) {
        this(algorithm.getValue(), SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  {@link SymmetricAlgorithm}
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public AsymmetricCrypto(AsymmetricAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
        this(algorithm.getValue(), privateKey, publicKey);
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  {@link SymmetricAlgorithm}
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @since 3.1.1
     */
    public AsymmetricCrypto(AsymmetricAlgorithm algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this(algorithm.getValue(), privateKey, publicKey);
    }

    /**
     * 构造 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm        非对称加密算法
     * @param privateKeyBase64 私钥Base64
     * @param publicKeyBase64  公钥Base64
     */
    public AsymmetricCrypto(String algorithm, String privateKeyBase64, String publicKeyBase64) {
        this(algorithm, Base64.getDecoder().decode(privateKeyBase64), Base64.getDecoder().decode(publicKeyBase64));
    }

    /**
     * 构造
     * <p>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  算法
     * @param privateKey 私钥
     * @param publicKey  公钥
     */
    public AsymmetricCrypto(String algorithm, byte[] privateKey, byte[] publicKey) {
        this(algorithm, //
                KeyUtil.generatePrivateKey(algorithm, privateKey), //
                KeyUtil.generatePublicKey(algorithm, publicKey)//
        );
    }

    /**
     * 构造
     * <p>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做加密或者解密
     *
     * @param algorithm  算法
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @since 3.1.1
     */
    public AsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithm, privateKey, publicKey);
    }
    // ------------------------------------------------------------------ Constructor end

    /**
     * 获取加密块大小
     *
     * @return 加密块大小
     */
    public int getEncryptBlockSize() {
        return encryptBlockSize;
    }

    /**
     * 设置加密块大小
     *
     * @param encryptBlockSize 加密块大小
     */
    public void setEncryptBlockSize(int encryptBlockSize) {
        this.encryptBlockSize = encryptBlockSize;
    }

    /**
     * 获取解密块大小
     *
     * @return 解密块大小
     */
    public int getDecryptBlockSize() {
        return decryptBlockSize;
    }

    /**
     * 设置解密块大小
     *
     * @param decryptBlockSize 解密块大小
     */
    public void setDecryptBlockSize(int decryptBlockSize) {
        this.decryptBlockSize = decryptBlockSize;
    }

    /**
     * 获取{@link AlgorithmParameterSpec}<br>
     * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
     *
     * @return {@link AlgorithmParameterSpec}
     * @since 5.4.3
     */
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return this.cipherWrapper.getParams();
    }

    /**
     * 设置{@link AlgorithmParameterSpec}<br>
     * 在某些算法中，需要特别的参数，例如在ECIES中，此处为IESParameterSpec
     *
     * @param algorithmParameterSpec {@link AlgorithmParameterSpec}
     * @since 5.4.3
     */
    public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
        this.cipherWrapper.setParams(algorithmParameterSpec);
    }

    /**
     * 设置随机数生成器，可自定义随机数种子
     *
     * @param random 随机数生成器，可自定义随机数种子
     * @return this
     * @since 5.7.17
     */
    public AsymmetricCrypto setRandom(SecureRandom random) {
        this.cipherWrapper.setRandom(random);
        return this;
    }

    @Override
    public AsymmetricCrypto init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super.init(algorithm, privateKey, publicKey);
        initCipher();
        return this;
    }

    // --------------------------------------------------------------------------------- Encrypt

    @Override
    public byte[] encrypt(byte[] data, KeyType keyType) {
        final Key key = getKeyByType(keyType);
        lock.lock();
        try {
            final Cipher cipher = initMode(Cipher.ENCRYPT_MODE, key);

            if (this.encryptBlockSize < 0) {
                // 在引入BC库情况下，自动获取块大小
                final int blockSize = cipher.getBlockSize();
                if (blockSize > 0) {
                    this.encryptBlockSize = blockSize;
                }
            }

            return getCipher().doFinal(data);
        } catch (Exception e) {
            throw new CryptoException(e);
        } finally {
            lock.unlock();
        }
    }

    // --------------------------------------------------------------------------------- Decrypt

    @Override
    public byte[] decrypt(byte[] data, KeyType keyType) {
        final Key key = getKeyByType(keyType);
        lock.lock();
        try {
            final Cipher cipher = initMode(Cipher.DECRYPT_MODE, key);

            if (this.decryptBlockSize < 0) {
                // 在引入BC库情况下，自动获取块大小
                final int blockSize = cipher.getBlockSize();
                if (blockSize > 0) {
                    this.decryptBlockSize = blockSize;
                }
            }

            return getCipher().doFinal(data);
        } catch (Exception e) {
            throw new CryptoException(e);
        } finally {
            lock.unlock();
        }
    }

    // --------------------------------------------------------------------------------- Getters and Setters

    /**
     * 获得加密或解密器
     *
     * @return 加密或解密
     * @since 5.4.3
     */
    public Cipher getCipher() {
        return this.cipherWrapper.getCipher();
    }

    /**
     * 初始化{@link Cipher}，默认尝试加载BC库
     *
     * @since 4.5.2
     */
    protected void initCipher() {
        this.cipherWrapper = new CipherWrapper(this.algorithm);
    }

    /**
     * 初始化{@link Cipher}的模式，如加密模式或解密模式
     *
     * @param mode 模式，可选{@link Cipher#ENCRYPT_MODE}或者{@link Cipher#DECRYPT_MODE}
     * @param key  密钥
     * @return {@link Cipher}
     * @throws InvalidAlgorithmParameterException 异常算法错误
     * @throws InvalidKeyException                异常KEY错误
     */
    private Cipher initMode(int mode, Key key) throws InvalidAlgorithmParameterException, InvalidKeyException {
        return this.cipherWrapper.initMode(mode, key).getCipher();
    }
}
