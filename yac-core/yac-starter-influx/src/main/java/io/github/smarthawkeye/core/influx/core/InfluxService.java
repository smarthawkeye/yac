package io.github.smarthawkeye.core.influx.core;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.smarthawkeye.core.influx.props.InfluxProperties;
import io.github.smarthawkeye.core.influx.pojo.*;
import io.github.smarthawkeye.core.influx.util.InfluxUtil;
import org.influxdb.InfluxDB;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * influx数据接口，只支持单表查询，不支持多表组合查询。
 * 查询条件定义：
 * 开始时间（startDate）：非必填，默认包含开始时间
 * 左包含（leftEqual）：非必填，默认为 true
 * 结束时间（endDate）：非必填，默认不包含结束时间
 * 右包含（rightEqual）：非必填，默认为 false
 * 数据单位（TimeUnit）：非必填，默认TimeUnit.MICROSECONDS(微秒）
 * 排序(sort)：非必填，默认true(正序)
 * <p>
 * <p>
 * 返回结果定义：
 * 返回数据时间：默认格式为yyyy-MM-dd HH:mm:ss,可自定义时间格式
 * 小数位数：非必填，默认保留两位小数。
 * 数据查找最大条数限制：50万条。
 *
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Component
public class InfluxService {
    @Resource
    InfluxTemplate influxTemplate;

    /**
     * 返回influx读对象
     */
    public InfluxDB getInfluxDBR() {
        return influxTemplate.getInfluxDBR();
    }

    /**
     * 返回influx写对象
     */
    public InfluxDB getInfluxDBW() {
        return influxTemplate.getInfluxDBW();
    }

    /**
     * 返回influx配置文件
     */
    public InfluxProperties getProperties() {
        return influxTemplate.properties;
    }

    /**
     * 数据，时间序列
     *
     * @param wrapper 查询定义
     */
    public TimeDataModel getTimeDataModel(QueryWrapper wrapper) {
        TimeDataModel dataModel = new TimeDataModel();
        List<List<Object>> datasList = influxTemplate.queryTimeDataList(dataModel.getQuotas(), wrapper);
        for (List<Object> datas : datasList) {
            //时间处理,第一列为时间
            String dataTimeStr = InfluxUtil.timeToString(new BigDecimal(datas.get(0).toString()).longValue(), wrapper.getTimeUnit(), wrapper.getDataTimePattern());
            //数据处理，第二列开始为数据
            for (int i = 1; i < datas.size(); i++) {
                Object data = datas.get(i);
                if (ObjectUtil.isNotNull(data)) {
                    dataModel.addData(i - 1, dataTimeStr, NumberUtil.round(data.toString(), wrapper.getFields().get(i - 1).getScale()));
                } else {
                    dataModel.addData(i - 1, dataTimeStr, data);
                }
            }
        }
        return dataModel;
    }

    /**
     * 数据序列和单时间序列
     *
     * @param wrapper 查询定义
     */
    public SingleTimeDataModel getSingleTimeDataModel(QueryWrapper wrapper) {
        SingleTimeDataModel dataModel = new SingleTimeDataModel();
        List<List<Object>> datasList = influxTemplate.queryTimeDataList(dataModel.getQuotas(), wrapper);
        for (List<Object> datas : datasList) {
            //时间处理,第一列为时间
            String dataTimeStr = InfluxUtil.timeToString(new BigDecimal(datas.get(0).toString()).longValue(), wrapper.getTimeUnit(), wrapper.getDataTimePattern());
            dataModel.addTime(dataTimeStr);
            //数据处理，第二列开始为数据
            for (int i = 1; i < datas.size(); i++) {
                Object data = datas.get(i);
                if (ObjectUtil.isNotNull(data)) {
                    dataModel.addData(i - 1, NumberUtil.round(data.toString(), wrapper.getFields().get(i - 1).getScale()));
                } else {
                    dataModel.addData(i - 1, data);
                }
            }
        }
        return dataModel;
    }

    /**
     * 指标数据结构序列
     *
     * @param wrapper 查询定义
     */
    public QuotaDataModel getQuotaDataModel(QueryWrapper wrapper) {
        QuotaDataModel dataModel = new QuotaDataModel();
        List<List<Object>> datasList = influxTemplate.queryTimeDataList(dataModel.getQuotas(), wrapper);
        for (List<Object> datas : datasList) {
            Map<String, Object> dataMaps = new HashMap<>();
            //时间处理,第一列为时间
            String dataTimeStr = InfluxUtil.timeToString(new BigDecimal(datas.get(0).toString()).longValue(), wrapper.getTimeUnit(), wrapper.getDataTimePattern());
            dataMaps.put(wrapper.getFields().get(0).getAsname(), dataTimeStr);
            //数据处理，第二列开始为数据
            for (int i = 1; i < datas.size(); i++) {
                Object data = datas.get(i);
                if (ObjectUtil.isNotNull(data)) {
                    dataMaps.put(wrapper.getFields().get(i).getAsname(), NumberUtil.round(data.toString(), wrapper.getFields().get(i).getScale()));
                } else {
                    dataMaps.put(wrapper.getFields().get(i).getAsname(), data);
                }
            }
            dataModel.addData(dataMaps);
        }
        return dataModel;
    }

    /**
     * 指数据结构序列分页 TODO 待完善
     *
     * @param wrapper 查询定义
     */
    public QuotaPageDataModel getQuotaPageDataModel(QueryWrapper wrapper) {
        QuotaPageDataModel dataModel = new QuotaPageDataModel();
        List<List<Object>> datasList = influxTemplate.queryTimeDataList(dataModel.getQuotas(), wrapper);
        for (List<Object> datas : datasList) {
            Map<String, Object> dataMaps = new HashMap<>();
            //时间处理,第一列为时间
            String dataTimeStr = InfluxUtil.timeToString(new BigDecimal(datas.get(0).toString()).longValue(), wrapper.getTimeUnit(), wrapper.getDataTimePattern());
            dataMaps.put(wrapper.getFields().get(0).getAsname(), dataTimeStr);
            //数据处理，第二列开始为数据
            for (int i = 1; i < datas.size(); i++) {
                Object data = datas.get(i);
                if (ObjectUtil.isNotNull(data)) {
                    dataMaps.put(wrapper.getFields().get(i).getAsname(), NumberUtil.round(data.toString(), wrapper.getFields().get(i).getScale()));
                } else {
                    dataMaps.put(wrapper.getFields().get(i).getAsname(), data);
                }
            }
            dataModel.addData(dataMaps);
        }
        return dataModel;
    }

    /**
     * 数据保存或更新
     *
     * @param wrapper
     */
    public void saveOrUpdate(UpdateWrapper wrapper) throws ParseException {
        if (wrapper.getFields().size() <= 0 || wrapper.getDatas().size() <= 0) {
            return;
        }
        influxTemplate.saveOrUpdate(wrapper);
    }
    /**
     * 数据删除
     * @param wrapper
     **/
    public void delete(DeleteWrapper wrapper) {
        influxTemplate.delete(wrapper);
    }
}
