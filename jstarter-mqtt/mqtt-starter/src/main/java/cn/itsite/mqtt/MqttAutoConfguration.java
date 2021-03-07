package cn.itsite.mqtt;

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
import org.springframework.util.StringUtils;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
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
        if (!StringUtils.isEmpty(mqttProperties.getUsername())
                && !StringUtils.isEmpty(mqttProperties.getPassword())) {
            mqttConnectOptions.setUserName(mqttProperties.getUsername());
            mqttConnectOptions.setPassword(mqttProperties.getPassword().toCharArray());
        }

        mqttConnectOptions.setServerURIs(mqttProperties.getUrls());
        mqttConnectOptions.setKeepAliveInterval(mqttProperties.getKeepAliveInterval());
        mqttConnectOptions.setConnectionTimeout(mqttProperties.getConnectionTimeout());
        mqttConnectOptions.setAutomaticReconnect(true);
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
        MqttProperties.Outbound outbound = mqttProperties.getOutbound();
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(outbound.getClientId(), mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(outbound.getTopics()[0]);
        return messageHandler;
    }

    /**********输入通道**********/
    @Bean(name = CHANNEL_INPUT)
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound(MqttPahoClientFactory mqttPahoClientFactory) {
        MqttProperties.Inbound inbound = mqttProperties.getInbound();
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(inbound.getUrl(), inbound.getClientId(), mqttPahoClientFactory, inbound.getTopics());
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setCompletionTimeout(mqttProperties.getConnectionTimeout());
        adapter.setRecoveryInterval(mqttProperties.getKeepAliveInterval());
        adapter.setOutputChannel(inputChannel());
        return adapter;
    }
}
