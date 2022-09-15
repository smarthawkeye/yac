package io.github.smarthawkeye.core.influx.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据格式2-数据序列和多时间序列：{"datas":[["2.0","3.0","4.0"],["2.0","3.0","4.0","4.0"]],"quotas":[{"name":"温度","tag":"wd","unit":"℃"},{"name":"应力","tag":"yl","unit":"KPa"}],"time":[["2022-06-1415:52:25","2022-06-1415:52:27","2022-06-1415:52:29"],["2022-06-1415:52:25","2022-06-1415:52:27","2022-06-1415:52:29","2022-06-1415:52:29"]]}
 *
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Deprecated
public class MultiTimeDataModel extends DataModel {
    /**
     * 第一层序列为数据时间组，第二层序列为数据时间序列，数据时间组数与指标和数据组数保持一致。
     */
    private List<List<Object>> time;

    /**
     * 第一层序列为数据组，第二层序列为数据序列
     */
    private List<List<Object>> datas;

    public MultiTimeDataModel() {
        super();
        this.time = new ArrayList<>();
        this.datas = new ArrayList<>();
    }

    /**
     * 添加时间
     *
     * @param dataTime
     */
    public void addTime(int index, Object dataTime) {
        if (this.time.size() <= index) {
            this.time.add(new ArrayList<Object>());
        }
        this.time.get(index).add(dataTime);
    }

    /**
     * 添加数据
     *
     * @param index
     * @param data
     */
    public void addData(int index, Object data) {
        if (this.datas.size() <= index) {
            this.datas.add(new ArrayList<Object>());
        }
        this.datas.get(index).add(data);
    }
}