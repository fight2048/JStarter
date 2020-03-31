## 简介

`mqtt-starter` 主要是对SpringBoot集成MQTT的二次封装。

## 使用

```xml
<dependency>
     <groupId>cn.itsite</groupId>
     <artifactId>mqtt-starter</artifactId>
     <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 配置
```yaml
mqtt:
  username:
  password:
  keepAliveInterval: 60
  connectionTimeout: 7200
  urls: tcp://127.0.0.1:1883
  inbound:
    clientId: client-inbound
    topics: topic/#
  outbound:
    clientId: client-outbound
    topics: topic1

```

### 使用

```

    @Autowired
    MqttGateway mqttGateway;

    @Autowired
    MqttPahoMessageDrivenChannelAdapter adapter;

    @PostMapping("/v1/messages")
    public Object messages(String topic, Integer qos, String payload) {
        mqttGateway.send(topic, qos, payload);
        return "OK";
    }

    @PostMapping("/v1/topic")
    public Object topics(String topic, Integer qos) {
        adapter.addTopic(topic, qos);
        return "OK";
    }
    
```