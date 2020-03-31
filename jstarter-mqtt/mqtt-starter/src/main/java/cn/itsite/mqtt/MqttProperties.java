package cn.itsite.mqtt;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = MqttProperties.MQTT_PREFIX)
public class MqttProperties {
    public static final String MQTT_PREFIX = "mqtt";

    private String username;
    private String password;
    private String[] urls;
    int keepAliveInterval = 60;
    int connectionTimeout = 7200;

    private Inbound inbound = new Inbound();
    private Outbound outbound = new Outbound();

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Inbound extends Properties {
        private String url;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Outbound extends Properties {
    }

    @Data
    public static class Properties {
        private String clientId;
        private String[] topics;
    }
}