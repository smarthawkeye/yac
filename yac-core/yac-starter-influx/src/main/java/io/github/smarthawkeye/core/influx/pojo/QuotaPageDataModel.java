package io.github.smarthawkeye.core.influx.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据结构序列分页
 * 数据格式4-指标数据结构序列分页：{"current":1,"datas":[{"s0":"0.0","time":"2022-06-1514:52:57","wd":"3.0","yl":"3.0"},{"s0":"0.0","time":"2022-06-1514:52:55","wd":"2.0","yl":"2.0"},{"s0":"0.0","time":"2022-06-1514:52:53","wd":"1.0","yl":"1.0"}],"pageCount":158,"quotas":[{"name":"采集时间","tag":"time"},{"name":"运行状态","tag":"s0"},{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}],"size":10,"total":1578}
 *
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
public class QuotaPageDataModel extends PageDataModel {
    /**
     * 第一层list为数据序列，Map为指标数据元素
     */
    private List<Map<String, Object>> datas;

    public QuotaPageDataModel() {
        super();
        this.datas = new ArrayList<>();
    }

    /**
     * 数据添加
     * @param data
     */
    public void addData(Map<String, Object> data) {
        datas.add(data);
    }

    /**
     * 数据添加
     * @param index
     * @param key
     * @param data
     */
    public void addData(int index, String key, Object data) {
        if (datas.size() <= index) {
            datas.add(new HashMap<String, Object>());
        }
        datas.get(index).put(key, data);
    }
}