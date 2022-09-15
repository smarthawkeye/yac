package io.github.smarthawkeye.core.rebbitmq.config;

import io.github.smarthawkeye.core.rebbitmq.props.RabbitProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitmq配置
 * @ClassName RabbitmqConfiguration
 * @Author xiaoya1
 * @Version V0.1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitmqConfiguration {
    @Autowired
    @SuppressWarnings("all")
    private RabbitProperties properties;

    @Bean
    public TopicExchange exchange() {
        log.debug("交换器:{}", properties.getExchange());
        /**持久化*/
        final boolean durable = true;
        /**不自动删除*/
        final boolean autoDelete = false;
        return new TopicExchange(properties.getExchange(), durable, autoDelete);
    }

    @Bean
    public Queue queue() {
        log.debug("队列:{}", "dangtu-data");
        /**持久化*/
        final boolean durable = true;
        /**不独占的*/
        final boolean exclusive = false;
        /**不自动删除*/
        final boolean autoDelete = false;
        final Map<String, Object> arguments = new HashMap<>();
        /**time to live (ms)*/
        arguments.put("x-message-ttl", 72 * 3600 * 1000);
        return new Queue("dangtu-data", durable, exclusive, autoDelete, arguments);
    }

    @Bean
    public Binding binding() {
        log.debug("绑定:{}", properties.getRoutingKey());
        return BindingBuilder.bind(queue()).to(exchange()).with(properties.getRoutingKey());
    }
}
