package cn.itsite.mqtt.api;

import cn.itsite.mqtt.MqttAutoConfguration;
import cn.itsite.mqtt.MqttGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2019/10/24/0024 0:25
 * @description:
 */
@Slf4j
@RestController
public class MqttController {

    @Autowired
    MqttGateway mqttGateway;

    @Autowired
    MqttPahoMessageDrivenChannelAdapter adapter;

    @PostMapping("/v1/messages")
    public Object messages(String topic, Integer qos, String payload) {
        mqttGateway.send(topic, qos, payload);
        return "OK";
    }

    @PostMapping("/v1/topics")
    public Object topics(String topic, Integer qos) {
        adapter.addTopic(topic, qos);
        return "OK";
    }

    @DeleteMapping("/v1/topics")
    public Object topics(String topic) {
        adapter.removeTopic(topic);
        return "OK";
    }

    @GetMapping("/v1/topics")
    public Object topics() {
        return adapter.getTopic();
    }

    @Bean
    @ServiceActivator(inputChannel = MqttAutoConfguration.CHANNEL_INPUT)
    public MessageHandler handler() {
        return message -> {
            log.info("handler--message-->" + message.getPayload());
        };
    }
}