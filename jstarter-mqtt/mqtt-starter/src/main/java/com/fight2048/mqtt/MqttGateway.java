package com.fight2048.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @version: v0.0.0
 * @blog: https://github.com/fight2048
 * @time: 2019/2/10 0010 23:51
 * @description:
 */
@Component
@MessagingGateway(defaultRequestChannel = "outputChannel")
public interface MqttGateway {

    void send(String payload);

    void send(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 发送信息到MQTT服务器
     *
     * @param topic   主题
     * @param qos     对消息处理的几种机制。
     *                0 表示的是订阅者没收到消息不会再次发送，消息会丢失。
     *                1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。
     *                2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param payload 消息主体
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}