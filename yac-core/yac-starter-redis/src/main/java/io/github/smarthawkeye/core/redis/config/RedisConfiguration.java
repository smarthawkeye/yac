package io.github.smarthawkeye.core.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.smarthawkeye.core.redis.core.RedisService;
import io.github.smarthawkeye.core.redis.core.RedisServiceImpl;
import io.github.smarthawkeye.core.redis.util.RedisLockUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName RedisConfiguration
 * @Description Redis基础配置类
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/26 11:46
 * @Version V0.1.0
 */
@Configuration
public class RedisConfiguration {
	@Bean
	public RedisSerializer<String> redisKeySerializer() {
		return RedisSerializer.string();
	}

	@Bean
	public RedisSerializer<Object> redisValueSerializer() {
		return RedisSerializer.json();
	}

	@SuppressWarnings("all")
	@Bean(name = "redisTemplate")
	@ConditionalOnClass(RedisOperations.class)
	public org.springframework.data.redis.core.RedisTemplate redisTemplate(RedisConnectionFactory factory) {
		org.springframework.data.redis.core.RedisTemplate template = new org.springframework.data.redis.core.RedisTemplate();
		template.setConnectionFactory(factory);

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	@ConditionalOnBean(name = "redisTemplate")
	public RedisService redisService() {
		return new RedisServiceImpl();
	}

	@Bean
	@ConditionalOnBean(name = "redisTemplate")
	public RedisLockUtil redisLockUtil(RedisTemplate redisTemplate) {
		return new RedisLockUtil(redisTemplate);
	}
}
