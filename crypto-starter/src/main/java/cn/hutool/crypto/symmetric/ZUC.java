package cn.hutool.crypto.symmetric;

import cn.hutool.crypto.KeyUtil;

import javax.crypto.spec.IvParameterSpec;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 祖冲之算法集（ZUC算法）实现，基于BouncyCastle实现。
 *
 * @author looly
 * @since 5.7.12
 */
public class ZUC extends SymmetricCrypto {
    private static final long serialVersionUID = 1L;

    /**
     * 生成ZUC算法密钥
     *
     * @param algorithm ZUC算法
     * @return 密钥
     * @see KeyUtil#generateKey(String)
     */
    public static byte[] generateKey(ZUCAlgorithm algorithm) {
        return KeyUtil.generateKey(algorithm.value).getEncoded();
    }

    /**
     * 构造
     *
     * @param algorithm ZUC算法枚举，包括128位和256位两种
     * @param key       密钥
     * @param iv        加盐，128位加盐是16bytes，256位是25bytes，{@code null}是随机加盐
     */
    public ZUC(ZUCAlgorithm algorithm, byte[] key, byte[] iv) {
        super(algorithm.value,
                KeyUtil.generateKey(algorithm.value, key),
                generateIvParam(algorithm, iv));
    }

    /**
     * ZUC类型，包括128位和256位
     *
     * @author looly
     */
    public enum ZUCAlgorithm {
        /**
         * 对应128位，16字节
         */
        ZUC_128("ZUC-128"),
        /**
         * 对应256位，25字节
         */
        ZUC_256("ZUC-256");

        private final String value;

        /**
         * 构造
         *
         * @param value 算法的字符串表示，区分大小写
         */
        ZUCAlgorithm(String value) {
            this.value = value;
        }

        /**
         * 获得算法的字符串表示形式
         *
         * @return 算法字符串
         */
        public String getValue() {
            return this.value;
        }
    }

    /**
     * 生成加盐参数
     *
     * @param algorithm ZUC算法
     * @param iv        加盐，128位加盐是16bytes，256位是25bytes，{@code null}是随机加盐
     * @return {@link IvParameterSpec}
     */
    private static IvParameterSpec generateIvParam(ZUCAlgorithm algorithm, byte[] iv) {
        if (null == iv) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            byte[] bytes;
            switch (algorithm) {
                case ZUC_128:
                    bytes = new byte[16];
                    random.nextBytes(bytes);
                    iv = bytes;
                    break;
                case ZUC_256:
                    bytes = new byte[25];
                    random.nextBytes(bytes);
                    iv = bytes;
                    break;
                default:
                    break;
            }
        }
        return new IvParameterSpec(iv);
    }
}
