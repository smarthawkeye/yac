package io.github.smarthawkeye.core.influx.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据格式3-数据，时间序列：{"datas":[[["2022-06-14 15:52:25","2.0"],["2022-06-14 15:52:27","3.0"],["2022-06-14 15:52:29","4.0"]],[["2022-06-14 15:51:00","5.0"],["2022-06-14 15:51:02","1.0"]]],"quotas":[{"name":"温度","tag":"wd","unit":"℃"},{"name":"温度","tag":"wd","unit":"℃"}]}
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
public class TimeDataModel extends DataModel {
    /**
     * 第一层序列为数据时间组，第二层序列为数据时间序列，数据时间组数与指标和数据组数保持一致。
     */
//    private List<List<Object>> time;
    /**
     * 第一层序列为数据组，第二层序列为数据序列，第三层为<时间，数据>结构
     */
    private List<List<List<Object>>> datas;

    public TimeDataModel() {
        super();
//        time = new ArrayList<>();
        datas = new ArrayList<>();
    }

    /**
     * 添加时间
     *
     * @param dataTime
     */
//    public void addTime(int index, Object dataTime) {
//        if (this.time.size() <= index) {
//            this.time.add(new ArrayList<Object>());
//        }
//        this.time.get(index).add(dataTime);
//    }

    /**
     *
     */
    public void addData(int index,Object dataTime,Object data){
        if(this.datas.size() <= index){
            this.datas.add(new ArrayList<List<Object>>());
        }
        List<Object> values = new ArrayList<>();
        values.add(dataTime);
        values.add(data);
        this.datas.get(index).add(values);
    }
}