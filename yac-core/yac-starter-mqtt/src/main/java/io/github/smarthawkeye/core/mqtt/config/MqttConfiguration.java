/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package io.github.smarthawkeye.core.mqtt.config;

import io.github.smarthawkeye.core.mqtt.core.MqttTemplate;
import io.github.smarthawkeye.core.mqtt.props.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AWS自动配置类
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfiguration {
    @Autowired
    @SuppressWarnings("all")
    private MqttProperties properties;
    private MqttClient client;
    @Bean
    @ConditionalOnMissingBean(MqttProperties.class)
    public MqttClient mqttClient() {
        try{
            //创建MQTT客户端对象
            this.client = new MqttClient(properties.getUrl(),properties.getClientId(),new MemoryPersistence());
            //连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置false表示服务器会保留客户端的连接记录（订阅主题，qos）,客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接服务器都是以新的身份
            options.setCleanSession(true);
            //设置断开重连
            options.setAutomaticReconnect(true);
            //设置连接用户名
            options.setUserName(properties.getUsername());
            //设置连接密码
            options.setPassword(properties.getPassword().toCharArray());
            //设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            //设置心跳时间 单位为秒，表示服务器每隔 1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
//            options.setWill("willTopic",(properties.getTopic() + "与服务器断开连接").getBytes(),0,false);
            //设置回调
//            client.setCallback(new YaMqttCallBack());
            client.connect(options);
        } catch(MqttException e){
            e.printStackTrace();
        }
        return client;
    }
    @Bean
    public MqttTemplate mqttTemplate() {
        return new MqttTemplate(client);
    }
}
