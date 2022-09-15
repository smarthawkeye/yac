package io.github.smarthawkeye.core.mqtt.core;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import javax.annotation.Resource;

/**
 * @ClassName MqttTemplate
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/24 14:46
 * @Version V0.1.0
 */
@Slf4j
public class MqttTemplate {
    /**
     * 客户端对象
     */
    @Resource
    private MqttClient client;
    public MqttTemplate(MqttClient client){
        this.client = client;
    }

    /**
     * 消息发布
     * @param qos 质量
     * @param retained 持久化
     * @param topic 主题
     * @param content 消息
     */
    public void publish(int qos,boolean retained,String topic,String content){
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        message.setRetained(retained);



//        mqttMessage.setPayload(message.getBytes());
        //主题的目的地，用于发布/订阅信息
       // MqttTopic mqttTopic = client.getTopic(topic);
        //提供一种机制来跟踪消息的传递进度
        //用于在以非阻塞方式（在后台运行）执行发布是跟踪消息的传递进度
       // MqttDeliveryToken token;
        try {
            client.publish("/yac_id/1",message);
            //将指定消息发布到主题，但不等待消息传递完成，返回的token可用于跟踪消息的传递状态
            //一旦此方法干净地返回，消息就已被客户端接受发布，当连接可用，将在后台完成消息传递。
         //   token = mqttTopic.publish(mqttMessage);
         //   token.waitForCompletion();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息订阅
     * @param topic 订阅主题
     * @param listener 消息监听回调
     */
    public void subscribe(String topic,IMqttMessageListener listener){
        try{
            client.subscribe(topic,listener);
        }catch (MqttException e) {
            log.error(String.format("MQTT: 订阅主题[%s]失败", topic));
        }
    }
}
