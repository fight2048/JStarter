package cn.itsite.email.api;

import cn.itsite.email.autoconfigure.AliyunEamilTemplate;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2019/10/24/0024 0:25
 * @description:
 */
@RestController
public class EmailController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    AliyunEamilTemplate eamilTemplate;

    @GetMapping("/emails")
    public Object aliyunEmail() throws ClientException {
        SingleSendMailRequest request = new SingleSendMailRequest();
        //request.setVersion(“2017-06-22”);// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
        request.setAccountName("edcrfv098k@mail.itsite.cn");
        request.setFromAlias("edcrfv098k");
        request.setAddressType(0);
//            request.setTagName("控制台创建的标签");
        request.setReplyToAddress(true);
        request.setToAddress("666233@qq.com");
        //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
        //request.setToAddress(“邮箱1,邮箱2”);
        request.setSubject("edcrfv098k");
        //如果采用byte[].toString的方式的话请确保最终转换成utf-8的格式再放入htmlbody和textbody，若编码不一致则会被当成垃圾邮件。
        //注意：文本邮件的大小限制为3M，过大的文本会导致连接超时或413错误
        request.setHtmlBody("111");
        //SDK 采用的是http协议的发信方式, 默认是GET方法，有一定的长度限制。
        //若textBody、htmlBody或content的大小不确定，建议采用POST方式提交，避免出现uri is not valid异常
        request.setMethod(MethodType.POST);
        SingleSendMailResponse send = eamilTemplate.send(request);
        return "success";
    }

    @GetMapping("/emails")
    public Object springbootEmail() throws ClientException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("langmanleguang@qq.com");
        message.setTo("langmanleguang@qq.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
        return "success";
    }


}