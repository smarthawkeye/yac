package io.github.smarthawkeye.core.redis.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisService
 * @Description 定义常用的 Redis操作
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/26 11:46
 * @Version V0.1.0
 */
public interface RedisService {

	/**
	 * 指定缓存失效时间
	 *
	 * @param key  键
	 * @param time 时间(秒)
	 * @return Boolean
	 */
	public Boolean expire(String key, Long time);

	/**
	 * 添加到带有 过期时间的  缓存
	 *
	 * @param key      redis主键
	 * @param value    值
	 * @param time     过期时间
	 * @param timeUnit 过期时间单位
	 */
	public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit);

	public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit, RedisSerializer<Object> valueSerializer);

	/**
	 * 根据key获取过期时间
	 *
	 * @param key 键 不能为 null
	 * @return 时间(秒) 返回 0代表为永久有效
	 */
	public Long getExpire(String key);

	/**
	 * 判断 key是否存在
	 *
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public Boolean hasKey(String key);

	/**
	 * 删除缓存
	 *
	 * @param key 可以传一个值 或多个
	 */
	public void del(String... key);

	/**
	 * 普通缓存获取
	 *
	 * @param key 键
	 * @return 值
	 */
	public Object get(String key);

	/**
	 * 根据key获取对象
	 *
	 * @param key             the key
	 * @param valueSerializer 序列化
	 * @return the string
	 */
	public Object get(final String key, RedisSerializer<Object> valueSerializer);

	/**
	 * 普通缓存放入
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public Boolean set(String key, Object value);


	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public Boolean set(String key, Object value, Long time);


	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key   键
	 * @param value 值
	 * @return true成功 false 失败
	 */
	public Boolean set(String key, Object value, Duration timeout);


	/**
	 * 递增
	 *
	 * @param key   键
	 * @param delta 要增加几(大于0)
	 * @return Long
	 */
	public Long incr(String key, Long delta);

	/**
	 * 递减
	 *
	 * @param key   键
	 * @param delta 要减少几(小于0)
	 * @return Long
	 */
	public Long decr(String key, Long delta) ;

	/**
	 * HashGet
	 *
	 * @param key  键 不能为 null
	 * @param item 项 不能为 null
	 * @return 值
	 */
	public Object hget(String key, String item);
	/**
	 * 获取 hashKey对应的所有键值
	 *
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<Object, Object> hmget(String key);
	/**
	 * HashSet
	 *
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public Boolean hmset(String key, Map<String, Object> map);

	/**
	 * HashSet 并设置时间
	 *
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public Boolean hmset(String key, Map<String, Object> map, Long time);

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public Boolean hset(String key, String item, Object value);

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public Boolean hset(String key, String item, Object value, Long time);
	/**
	 * 删除hash表中的值
	 *
	 * @param key  键 不能为 null
	 * @param item 项 可以使多个不能为 null
	 */
	public void hdel(String key, Object... item);
	/**
	 * 判断hash表中是否有该项的值
	 *
	 * @param key  键 不能为 null
	 * @param item 项 不能为 null
	 * @return true 存在 false不存在
	 */
	public Boolean hHasKey(String key, String item) ;
	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return Double
	 */
	public Double hincr(String key, String item, Double by);

	/**
	 * hash递减
	 *
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return Double
	 */
	public Double hdecr(String key, String item, Double by);
	/**
	 * 根据 key获取 Set中的所有值
	 *
	 * @param key 键
	 * @return Set
	 */
	public Set<Object> sGet(String key);

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public Boolean sHasKey(String key, Object value) ;

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public Long sSet(String key, Object... values);

	/**
	 * 将set数据放入缓存
	 *
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public Long sSetAndTime(String key, Long time, Object... values);

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return Long
	 */
	public Long sGetSetSize(String key);

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public Long setRemove(String key, Object... values);
	/**
	 * 获取list缓存的内容
	 *
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return List
	 */
	public List<Object> lGet(String key, Long start, Long end);

	/**
	 * 获取list缓存的长度
	 *
	 * @param key 键
	 * @return Long
	 */
	public Long lGetListSize(String key);

	/**
	 * 通过索引 获取list中的值
	 *
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；
	 *              index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return Object
	 */
	public Object lGetIndex(String key, Long index);

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return Boolean
	 */
	public Boolean lSet(String key, Object value);

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return Boolean
	 */
	public Boolean lSet(String key, Object value, Long time);

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return Boolean
	 */
	public Boolean lSet(String key, List<Object> value);

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return Boolean
	 */
	public Boolean lSet(String key, List<Object> value, Long time);
	/**
	 * 根据索引修改list中的某条数据
	 *
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return Boolean
	 */
	public Boolean lUpdateIndex(String key, Long index, Object value);

	/**
	 * 移除N个值为value
	 *
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public Long lRemove(String key, Long count, Object value);
	/**
	 * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
	 *
	 * @param key             the key
	 * @param start           the start
	 * @param end             the end
	 * @param valueSerializer 序列化
	 * @return the list
	 */
	public List<Object> getList(String key, int start, int end, RedisSerializer<Object> valueSerializer);

	/**
	 * 分布式锁
	 *
	 * @param key        分布式锁key
	 * @param expireTime 持有锁的最长时间 (redis过期时间) 秒为单位
	 * @return 返回获取锁状态 成功失败
	 */
	public boolean tryLock(String key, int expireTime);
	public void unLock(String key);
}
