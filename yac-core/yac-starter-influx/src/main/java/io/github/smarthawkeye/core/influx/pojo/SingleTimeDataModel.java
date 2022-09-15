package io.github.smarthawkeye.core.influx.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 单时间序列
 * 数据格式1-数据序列和单时间序列：{"datas":[["2.0","3.0","4.0"],["2.0","3.0","4.0"]],"quotas":[{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}],"time":["2022-06-1415:52:25","2022-06-1415:52:27","2022-06-1415:52:29"]}
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
public class SingleTimeDataModel extends DataModel{
    /**
     * 时间序列
     */
    private List<Object> time;
    /**
     * 第一层序列为数据组，第二层序列为数据序列
     */
    private List<List<Object>> datas;

    public SingleTimeDataModel() {
        super();
        this.time = new ArrayList<>();
        this.datas = new ArrayList<>();
    }

    /**
     * 添加时间
     * @param dataTime
     */
    public void addTime(Object dataTime){
        this.time.add(dataTime);
    }

    /**
     * 添加数据
     * @param index
     * @param data
     */
    public void addData(int index,Object data){
        if(this.datas.size() <= index){
            this.datas.add(new ArrayList<Object>());
        }
        this.datas.get(index).add(data);
    }
}
