package io.github.smarthawkeye.core.influx.pojo;

import io.github.smarthawkeye.core.influx.common.YacCostant;
import lombok.Data;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UpdateWrapper
 * @Description 描述
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/9/13 15:16
 * @Version V0.1.0
 */
@Data
public class UpdateWrapper {
    /**
     * 必填，数据表名称
     */
    private String measurement;
    /**
     * 必填，fields
     */
    private List<Field> fields;
    /**
     * 非必填，tags
     */
    private Map<String,String> tags;
    /**
     * 非必填，数据单位（TimeUnit）:默认TimeUnit.MICROSECONDS(微秒）
     */
    private TimeUnit timeUnit;

    /**
     * 时间戳序列
     */
    private List<Long> times;
    /**
     * 数据序列
     */
    private List<List<Object>> datas;
    public UpdateWrapper() {
        this.fields = new ArrayList<>();
        this.tags = new HashMap<>();
        this.timeUnit = TimeUnit.MICROSECONDS;
        times = new ArrayList<>();
        datas = new ArrayList<>();
    }
    public void addField(Field field){
        this.fields.add(field);
    }
    public void add(Long time,List<Object> values){
        times.add(time);
        datas.add(values);
    }
    public void add(Long time,Object... values){
        times.add(time);
        List<Object> objs = new ArrayList<>();
        for (Object value : values) {
            objs.add(value);
        }
        datas.add(objs);
    }
}
