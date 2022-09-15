package io.github.smarthawkeye.core.influx.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据格式1-数据序列和单时间序列：{"datas":[["2.0","3.0","4.0"],["2.0","3.0","4.0"]],"quotas":[{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}],"time":["2022-06-1415:52:25","2022-06-1415:52:27","2022-06-1415:52:29"]}
 * 数据格式2-数据序列和多时间序列：{"datas":[["2.0","3.0","4.0"],["2.0","3.0","4.0","4.0"]],"quotas":[{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}],"time":[["2022-06-1415:52:25","2022-06-1415:52:27","2022-06-1415:52:29"],["2022-06-1415:52:25","2022-06-1415:52:27","2022-06-1415:52:29","2022-06-1415:52:29"]]}
 * 数据格式3-数据，时间序列：{"datas":[[["2022-06-14 15:52:25","2.0"],["2022-06-14 15:52:27","3.0"],["2022-06-14 15:52:29","4.0"]],[["2022-06-14 15:51:00","5.0"],["2022-06-14 15:51:02","1.0"]]],"quotas":[{"name":"温度","tag":"wd","unit":"℃"},{"name":"温度","tag":"wd","unit":"℃"}]}
 * 数据格式4-数据结构序列分页：{"current":1,"datas":[{"s0":"0.0","time":"2022-06-15 14:52:57","wd":"3.0","yl":"3.0"},{"s0":"0.0","time":"2022-06-1514:52:55","wd":"2.0","yl":"2.0"},{"s0":"0.0","time":"2022-06-1514:52:53","wd":"1.0","yl":"1.0"}],"pageCount":158,"quotas":[{"name":"采集时间","tag":"time"},{"name":"运行状态","tag":"s0"},{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}],"size":10,"total":1578}
 * 数据格式5-数据结构序列：{"datas":[{"time":"2022-06-1514:52:57","wd":"3.0","yl":"3.0"},{"time":"2022-06-1514:52:55","wd":"2.0","yl":"2.0"},{"time":"2022-06-1514:52:53","wd":"1.0","yl":"1.0"}],"quotas":[{"name":"采集时间","tag":"time"},{"name":"运行状态","tag":"s0"},{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}]}
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
public abstract class DataModel implements Serializable{
    /**
     * 指标列表
     */
    private List<Quota> quotas;
    public DataModel(){
        quotas = new ArrayList<>();
    }
    /**
     * 添加指标
     * @param quota
     */
    public void addQuota(Quota quota){
        this.quotas.add(quota);
    }

    public void addQuota(Field queryField){
        quotas.add(new Quota(queryField.getChineseName(),queryField.getAsname(),queryField.getUnit()));
    }
}
