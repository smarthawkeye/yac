package com.yac.example.rabbit.sevice;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @ClassName MessageListener
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/24 18:01
 * @Version V0.1.0
 */
public class MessageListener implements IMqttMessageListener {
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println("recive mqttMessage = " + mqttMessage);
    }
}
