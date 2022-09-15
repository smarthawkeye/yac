package io.github.smarthawkeye.core.celldb.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.smarthawkeye.core.celldb.pojo.Meta;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 缓存
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Slf4j
public class CellCache {
    private AtomicInteger countCache;
//    private Cache<String,Integer> dbCount = CacheBuilder.newBuilder()
//            //设置缓存初始大小，应该合理设置，后续会扩容
//            .initialCapacity(5)
//            //最大值
//            .maximumSize(512)
//            //并发数设置
//            .concurrencyLevel(5)
//            //缓存写入过期时间，永不过期
//            .expireAfterWrite(365 * 99, TimeUnit.DAYS)
//            // 此缓存对象经过多少秒没有被访问则过期，永不过期
//            .expireAfterAccess(365 * 99,TimeUnit.DAYS)
//            //开启统计缓存命中率开关 通过cache.stats()信息查看统计结果
//            .recordStats()
//            .build();
    private Cache<String, Meta> dbCache = CacheBuilder.newBuilder()
            //设置缓存初始大小，应该合理设置，后续会扩容
            .initialCapacity(10)
            //最大值
            .maximumSize(512)
            //并发数设置
            .concurrencyLevel(5)
            //缓存写入过期时间，永不过期
            .expireAfterWrite(365 * 99, TimeUnit.DAYS)
            // 此缓存对象经过多少秒没有被访问则过期，永不过期
            .expireAfterAccess(365 * 99,TimeUnit.DAYS)
            //开启统计缓存命中率开关 通过cache.stats()信息查看统计结果
            .recordStats()
            .build();
    public Meta getMeta(String db) {
        Meta meta = null;
        try {
            meta = dbCache.get(db, () -> {
                return null;
            });
        }catch(Exception ex){
            log.error("CellCache cache read field, db ={}.",db);
        }
        return meta;
    }
    public List<Meta> dbList() {
        return new ArrayList<Meta>(dbCache.asMap().values());
    }
    public void putMeta(String db, Meta meta) {
        dbCache.put(db,meta);
    }

//    public int getDBCount() {
//        int count = 0;
//        try {
//            count = dbCount.get("dbcount", () -> {
//                return 0;
//            });
//        }catch(Exception ex){
//            log.error("dbCount cache read field.");
//        }
//        return count;
//    }
//    public void putDBCount(Integer count) {
//        dbCount.put("dbcount",count);
//    }
    public void countInit(int count){
        countCache = new AtomicInteger(count);
    }
    public int getCount(){
        return countCache.incrementAndGet();
    }
}
