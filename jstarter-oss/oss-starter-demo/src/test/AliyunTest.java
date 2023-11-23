import com.fight2048.oss.OssApplication;
import com.fight2048.oss.autoconfigure.aliyun.AliyunOssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @blog: https://github.com/fight2048
 * @time: 2019-01-08 0008 下午 11:17
 * @version: v0.0.0
 * @description:
 */
@Slf4j
@SpringBootTest(classes = OssApplication.class)
class AliyunTest {
    @Autowired
    private AliyunOssTemplate template;

    @Test
    void upload() {
    }
}