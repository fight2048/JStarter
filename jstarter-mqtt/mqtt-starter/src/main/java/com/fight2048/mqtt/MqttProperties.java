package com.fight2048.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: fight2048
 * @e-mail: fight2048@outlook.com
 * @version: v0.0.0
 * @blog: https://github.com/fight2048
 * @time: 2019/2/10 0010 23:51
 * @description:
 */
@ConfigurationProperties(prefix = MqttProperties.MQTT_PREFIX)
public class MqttProperties {
    public static final String MQTT_PREFIX = "mqtt";
    private String username;
    private String password;
    private String[] urls;
    private int keepAliveInterval = 60;
    private int connectionTimeout = 7200;

    private Inbound inbound = new Inbound();
    private Outbound outbound = new Outbound();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Inbound getInbound() {
        return inbound;
    }

    public void setInbound(Inbound inbound) {
        this.inbound = inbound;
    }

    public Outbound getOutbound() {
        return outbound;
    }

    public void setOutbound(Outbound outbound) {
        this.outbound = outbound;
    }

    public static class Inbound extends Properties {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Outbound extends Properties {
    }

    public static class Properties {
        private String clientId;
        private String[] topics;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String[] getTopics() {
            return topics;
        }

        public void setTopics(String[] topics) {
            this.topics = topics;
        }
    }
}