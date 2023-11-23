import com.aliyun.oss.common.comm.Protocol;
import org.junit.jupiter.api.Test;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2019-01-08 0008 下午 11:17
 * @version: v0.0.0
 * @description:
 */
class CommonTest {

    @Test
    void time() {
        for (Protocol value : Protocol.values()) {
            System.out.println(value.toString());
        }
        System.out.println(Protocol.HTTP);
        System.out.println(Protocol.valueOf("HTTP"));
    }
}