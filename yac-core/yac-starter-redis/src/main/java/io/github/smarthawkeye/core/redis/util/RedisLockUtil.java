package io.github.smarthawkeye.core.redis.util;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisLockUtil
 * @Description Redis分布式锁
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/26 11:46
 * @Version V0.1.0
 */
public class RedisLockUtil {

	public RedisLockUtil(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	private RedisTemplate<String, Object> redisTemplate;

	private static final byte[] SCRIPT_RELEASE_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end".getBytes();

	/**
	 * 尝试获取分布式锁
	 *
	 * @param key       键
	 * @param requestId 请求ID
	 * @param expire    锁的有效时间（秒）
	 */
	public synchronized Boolean tryLock(String key, String requestId, long expire) {
		return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.set(key.getBytes(), requestId.getBytes(), Expiration.from(expire, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
	}

	/**
	 * 释放分布式锁
	 *
	 * @param key       键
	 * @param requestId 请求ID
	 */
	public synchronized Boolean releaseLock(String key, String requestId) {
		return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.eval(SCRIPT_RELEASE_LOCK, ReturnType.BOOLEAN, 1, key.getBytes(), requestId.getBytes()));
	}

}
