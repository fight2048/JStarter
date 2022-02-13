package cn.hutool.crypto;

import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 签名工具类<br>
 * 封装包括：
 * <ul>
 *     <li>非堆成签名，签名算法支持见{@link SignAlgorithm}</li>
 *     <li>对称签名，支持Map类型参数排序后签名</li>
 *     <li>摘要签名，支持Map类型参数排序后签名，签名方法见：{@link DigestAlgorithm}</li>
 * </ul>
 *
 * @author looly
 * @since 5.7.20
 */
public class SignUtil {

    /**
     * 创建签名算法对象<br>
     * 生成新的私钥公钥对
     *
     * @param algorithm 签名算法
     * @return {@link Sign}
     * @since 3.3.0
     */
    public static Sign sign(SignAlgorithm algorithm) {
        return new Sign(algorithm);
    }

    /**
     * 创建签名算法对象<br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
     *
     * @param algorithm        签名算法
     * @param privateKeyBase64 私钥Base64
     * @param publicKeyBase64  公钥Base64
     * @return {@link Sign}
     * @since 3.3.0
     */
    public static Sign sign(SignAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64) {
        return new Sign(algorithm, privateKeyBase64, publicKeyBase64);
    }

    /**
     * 创建Sign算法对象<br>
     * 私钥和公钥同时为空时生成一对新的私钥和公钥<br>
     * 私钥和公钥可以单独传入一个，如此则只能使用此钥匙来做签名或验证
     *
     * @param algorithm  算法枚举
     * @param privateKey 私钥
     * @param publicKey  公钥
     * @return {@link Sign}
     * @since 3.3.0
     */
    public static Sign sign(SignAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
        return new Sign(algorithm, privateKey, publicKey);
    }

    /**
     * 对参数做签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
     * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
     *
     * @param crypto      对称加密算法
     * @param params      参数
     * @param otherParams 其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.1
     */
    public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String... otherParams) {
        return signParams(crypto, params, "", "", true, otherParams);
    }

    /**
     * 对参数做签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
     *
     * @param crypto            对称加密算法
     * @param params            参数
     * @param separator         entry之间的连接符
     * @param keyValueSeparator kv之间的连接符
     * @param isIgnoreNull      是否忽略null的键和值
     * @param otherParams       其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.1
     */
    public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String separator,
                                    String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        return crypto.encryptHex(join(sort(params, null), separator, keyValueSeparator, isIgnoreNull, otherParams));
    }

    /**
     * 对参数做md5签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
     * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
     *
     * @param params      参数
     * @param otherParams 其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.1
     */
    public static String signParamsMd5(Map<?, ?> params, String... otherParams) {
        return signParams(DigestAlgorithm.MD5, params, otherParams);
    }

    /**
     * 对参数做Sha1签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
     * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
     *
     * @param params      参数
     * @param otherParams 其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.8
     */
    public static String signParamsSha1(Map<?, ?> params, String... otherParams) {
        return signParams(DigestAlgorithm.SHA1, params, otherParams);
    }

    /**
     * 对参数做Sha256签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
     * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
     *
     * @param params      参数
     * @param otherParams 其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.1
     */
    public static String signParamsSha256(Map<?, ?> params, String... otherParams) {
        return signParams(DigestAlgorithm.SHA256, params, otherParams);
    }

    /**
     * 对参数做签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串<br>
     * 拼接后的字符串键值对之间无符号，键值对之间无符号，忽略null值
     *
     * @param digestAlgorithm 摘要算法
     * @param params          参数
     * @param otherParams     其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.1
     */
    public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String... otherParams) {
        return signParams(digestAlgorithm, params, "", "", true, otherParams);
    }

    /**
     * 对参数做签名<br>
     * 参数签名为对Map参数按照key的顺序排序后拼接为字符串，然后根据提供的签名算法生成签名字符串
     *
     * @param digestAlgorithm   摘要算法
     * @param params            参数
     * @param separator         entry之间的连接符
     * @param keyValueSeparator kv之间的连接符
     * @param isIgnoreNull      是否忽略null的键和值
     * @param otherParams       其它附加参数字符串（例如密钥）
     * @return 签名
     * @since 4.0.1
     */
    public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String separator,
                                    String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        return new Digester(digestAlgorithm)
                .digestHex(join(sort(params, null), separator, keyValueSeparator, isIgnoreNull, otherParams));
    }

    /**
     * 排序已有Map，Key有序的Map
     *
     * @param <K>        key的类型
     * @param <V>        value的类型
     * @param map        Map，为null返回null
     * @param comparator Key比较器
     * @return TreeMap，map为null返回null
     * @since 4.0.1
     */
    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        if (null == map) {
            return null;
        }

        if (map instanceof TreeMap) {
            // 已经是可排序Map，此时只有比较器一致才返回原map
            TreeMap<K, V> result = (TreeMap<K, V>) map;
            if (null == comparator || comparator.equals(result.comparator())) {
                return result;
            }
        }

        final TreeMap<K, V> treeMap = new TreeMap<>(comparator);
        treeMap.putAll(map);
        return treeMap;
    }

    /**
     * 将map转成字符串
     *
     * @param <K>               键类型
     * @param <V>               值类型
     * @param map               Map，为空返回otherParams拼接
     * @param separator         entry之间的连接符
     * @param keyValueSeparator kv之间的连接符
     * @param isIgnoreNull      是否忽略null的键和值
     * @param otherParams       其它附加参数字符串（例如密钥）
     * @return 连接后的字符串，map和otherParams为空返回""
     * @since 3.1.1
     */
    public static <K, V> String join(Map<K, V> map, String separator, String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        final StringBuilder strBuilder = new StringBuilder();
        boolean isFirst = true;
        if (Objects.nonNull(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (!isIgnoreNull || entry.getKey() != null && entry.getValue() != null) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        strBuilder.append(separator);
                    }
                    strBuilder.append(entry.getKey().toString()).append(keyValueSeparator).append(entry.getValue().toString());
                }
            }
        }
        // 补充其它字符串到末尾，默认无分隔符
        if (Objects.nonNull(otherParams)) {
            for (String otherParam : otherParams) {
                strBuilder.append(otherParam);
            }
        }
        return strBuilder.toString();
    }
}
