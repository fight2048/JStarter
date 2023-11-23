package com.fight2048.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @version: v0.0.0
 * @blog: https://github.com/fight2048
 * @time: 2019/2/10 0010 23:51
 * @description:
 */
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttAutoConfguration {
    public static final String CHANNEL_OUTPUT = "outputChannel";
    public static final String CHANNEL_INPUT = "inputChannel";

    @Autowired
    private MqttProperties mqttProperties;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);

        if (Objects.isNull(mqttProperties)) {
            return mqttConnectOptions;
        }
        if (!ObjectUtils.isEmpty(mqttProperties.getUsername())) {
            mqttConnectOptions.setUserName(mqttProperties.getUsername());
        }
        if (!ObjectUtils.isEmpty(mqttProperties.getPassword())) {
            mqttConnectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        }

        if (Objects.nonNull(mqttProperties.getUrls())) {
            mqttConnectOptions.setServerURIs(mqttProperties.getUrls());
        }

        if (Objects.nonNull(mqttProperties.getKeepAliveInterval())) {
            mqttConnectOptions.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        }

        if (Objects.nonNull(mqttProperties.getConnectionTimeout())) {
            mqttConnectOptions.setConnectionTimeout(mqttProperties.getConnectionTimeout());
        }

        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory(MqttConnectOptions mqttConnectOptions) {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    @Bean(name = CHANNEL_OUTPUT)
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = CHANNEL_OUTPUT)
    public MessageHandler outbound(MqttPahoClientFactory mqttPahoClientFactory) {
        Assert.notNull(mqttProperties, "'mqttProperties' must not be null");
        MqttProperties.Outbound outbound = mqttProperties.getOutbound();
        Assert.notNull(outbound, "'mqttProperties.outbound' must not be null");

        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(outbound.getClientId(), mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(outbound.getTopic());
        return messageHandler;
    }

    /**********输入通道**********/
    @Bean(name = CHANNEL_INPUT)
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound(MqttPahoClientFactory mqttPahoClientFactory) {
        Assert.notNull(mqttProperties, "'mqttProperties' must not be null");
        MqttProperties.Inbound inbound = mqttProperties.getInbound();
        Assert.notNull(inbound, "'mqttProperties.inbound' must not be null");

        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(inbound.getUrl(), inbound.getClientId(), mqttPahoClientFactory, inbound.getTopics());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(inbound.getQos());

//        if (Objects.nonNull(mqttProperties.getKeepAliveInterval())) {
//            adapter.setRecoveryInterval(mqttProperties.getKeepAliveInterval());
//        }

        if (Objects.nonNull(mqttProperties.getConnectionTimeout())) {
            adapter.setCompletionTimeout(mqttProperties.getConnectionTimeout());
        }

        adapter.setOutputChannel(inputChannel());
        return adapter;
    }
}
