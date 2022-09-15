package io.github.smarthawkeye.core.influx.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.smarthawkeye.core.influx.common.DataType;
import io.github.smarthawkeye.core.influx.common.YacCostant;
import io.github.smarthawkeye.core.influx.pojo.*;
import io.github.smarthawkeye.core.influx.props.InfluxProperties;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Component
public class InfluxTemplate {
    @Resource
    private InfluxDB influxDBR;
    @Resource
    private InfluxDB influxDBW;
    @Resource
    InfluxProperties properties;

    /**
     * 返回influx读对象
     */
    public InfluxDB getInfluxDBR() {
        return influxDBR;
    }

    /**
     * 返回influx写对象
     */
    public InfluxDB getInfluxDBW() {
        return influxDBW;
    }

    /**
     * 返回influx配置文件
     */
    public InfluxProperties getProperties() {
        return properties;
    }

    public List<List<Object>> queryTimeDataList(List<Quota> quotas, QueryWrapper wrapper) {
        String sql = sqlAssemble(wrapper);
        System.out.println("sql = " + sql);
        Query query = new Query(sql, properties.getDatabase());
        influxDBR.setLogLevel(InfluxDB.LogLevel.NONE);
        //毫秒输出
        QueryResult queryResult = influxDBR.query(query, wrapper.getTimeUnit());
        List<QueryResult.Result> resultList = queryResult.getResults();
        //数据组装
        if (resultList != null && resultList.size() > 0) {
            List<QueryResult.Series> seriesList = resultList.get(0).getSeries();
            if (seriesList != null && seriesList.size() > 0) {
                QueryResult.Series series = seriesList.get(0);
                //设置dataModel  quotas
                List<String> columns = series.getColumns();
                //去除time标签
                int columnIndex = 1;
                for (int i = 0; i < wrapper.getFields().size(); i++) {
                    Field queryField = wrapper.getFields().get(i);
                    if (queryField.getAsname().equals(columns.get(columnIndex))) {
                        quotas.add(new Quota(queryField));
                        columnIndex++;
                    }
                }
                return series.getValues();
            }
        }
        return new ArrayList<>();
    }

    /**
     * sql组装
     *
     * @param wrapper
     */
    public String sqlAssemble(QueryWrapper wrapper) {
        //组装SQL语句
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        //field sql组装
        List<Field> fields = wrapper.getFields();
        for (Field field : fields) {
            sql.append(field.getQueryName())
                    .append(" as ")
                    .append(field.getAsname())
                    .append(" ,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append("FROM ")
                .append(wrapper.getMeasurement())
                .append(" WHERE 1=1 ");
        //tags sql组装
        AtomicInteger increment = new AtomicInteger(1);
        int tagSize = wrapper.getTags().size();
        for (int i = 0; i < tagSize; i++) {
            if (i == 0) {
                //第一个元素
                sql.append(" and ( ");
            }
            if (wrapper.getTags().get(i).getValue() instanceof String) {
                sql.append(wrapper.getTags().get(i).getName()).append("='").append(wrapper.getTags().get(i).getValue()).append("' ");
            } else {
                sql.append(wrapper.getTags().get(i).getName()).append("=").append(wrapper.getTags().get(i).getValue()).append(" ");
            }
            if (i < tagSize - 1) {
                sql.append(wrapper.getTagConnector()).append(" ");
            } else {
                sql.append(") ");
            }
        }
        //开始时间
        if (ObjectUtil.isNotNull(wrapper.getStartTime())) {
            String equal = wrapper.isLeftEqual() ? "=" : "";
            sql.append("AND time >").append(equal).append(" '").append(DateUtil.format(wrapper.getStartTime(), YacCostant.FMT_MILLI_STD)).append("' ");
        }

        if (ObjectUtil.isNotNull(wrapper.getEndTime())) {
            String equal = wrapper.isRightEqual() ? "=" : "";
            sql.append("AND time <").append(equal).append(" '").append(DateUtil.format(wrapper.getEndTime(), YacCostant.FMT_MILLI_STD)).append("' ");
        }
        sql.append("ORDER BY time ");
        if (wrapper.isSort()) {
            sql.append(YacCostant.INFLUX_SORT_ASC).append(" ");
        } else {
            sql.append(YacCostant.INFLUX_SORT_DESC).append(" ");
        }
        sql.append("LIMIT ").append(wrapper.getLimit()).append(" ")
                .append("OFFSET ").append((wrapper.getCurrent() - 1) * wrapper.getLimit()).append(" ");
        sql.append("tz('Asia/Shanghai')");
        return sql.toString();
    }

    /**
     * 数据保存
     *
     * @param wrapper
     */
    public void saveOrUpdate(UpdateWrapper wrapper) {
        BatchPoints batchPoints = getBatchPoints();
        try {
            int index = 0;
            List<List<Object>> datas = wrapper.getDatas();
            for (int j = 0; j < datas.size(); j++) {
                List<Object> objects = datas.get(j);
                if (objects.size() == wrapper.getFields().size()) {
                    long timestamp = wrapper.getTimes().get(j);
                    Point.Builder pointBuilder = getPointBuilder(wrapper.getMeasurement(), wrapper.getTags(), wrapper.getTimeUnit(), timestamp);
                    for (int i = 0; i < objects.size(); i++) {
                        Field field = wrapper.getFields().get(i);
                        switch (field.getDataType()) {
                            case BOOLEAN:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), Boolean.parseBoolean(objects.get(i).toString()));
                                break;
                            case LONG:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), Long.parseLong(objects.get(i).toString()));
                                break;
                            case DOUBLE:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), Double.parseDouble(objects.get(i).toString()));
                                break;
                            case INT:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), Integer.parseInt(objects.get(i).toString()));
                                break;
                            case FLOAT:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), Float.parseFloat(objects.get(i).toString()));
                                break;
                            case SHORT:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), Short.parseShort(objects.get(i).toString()));
                                break;
                            case STRING:
                                pointBuilder.addField(wrapper.getFields().get(i).getAsname(), objects.get(i).toString());
                                break;

                        }
                    }
                    batchPoints.point(pointBuilder.build());
                    index++;
//                    int size = batchPoints.getPoints().size();
                    // 每读取五万条数据 提交到 influxDB 存储一次
                    if (index > 50000) {
                        index = 0;
                        influxDBW.write(batchPoints);
                        batchPoints.getPoints().clear();
                    }
                }
            }
            if (index > 0) {
                influxDBW.write(batchPoints);
                batchPoints.getPoints().clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            batchPoints.getPoints().clear();
        }

    }

    private Point.Builder getPointBuilder(String measurement, Map<String, String> tags, TimeUnit timeUnit, long timestamp) throws ParseException {
        if (ObjectUtil.isNotNull(tags)) {
            return Point.measurement(measurement)
                    .time(timestamp, timeUnit).tag(tags);
        }
        return Point.measurement(measurement)
                .time(timestamp, timeUnit);
    }

    private BatchPoints getBatchPoints() {
        return BatchPoints
                .database(properties.getDatabase())
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
    }
    /**
     * 数据保存
     */
    public void delete(DeleteWrapper wrapper) {
        //组装SQL语句
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE ");
        sql.append("FROM ")
                .append(wrapper.getMeasurement())
                .append(" WHERE ")
        .append("time >=").append(wrapper.getStartTime()).append(" AND time <= ").append(wrapper.getEndTime());
        wrapper.getTags().forEach((tag ,val)->{
            sql.append(" AND ").append(tag).append("='").append(val).append("' ");
        });
        Query query = new Query(sql.toString(), properties.getDatabase());
        influxDBR.setLogLevel(InfluxDB.LogLevel.NONE);
        //毫秒输出
        QueryResult queryResult = influxDBR.query(query);
        List<QueryResult.Result> resultList = queryResult.getResults();
        //数据组装
        if (resultList != null && resultList.size() > 0) {
            System.out.println("resultList = " + resultList);
        }
    }
}
