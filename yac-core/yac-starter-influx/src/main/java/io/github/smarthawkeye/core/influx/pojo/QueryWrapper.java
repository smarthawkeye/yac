package io.github.smarthawkeye.core.influx.pojo;


import io.github.smarthawkeye.core.influx.common.YacCostant;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class QueryWrapper {
    /**
     * 必填，数据表名称
     */
    private String measurement;
    /**
     * 非必填，开始时间,默认包含开始时间
     */
    private Date startTime;
    /**
     * 非必填，左包含（leftEqual）：默认为 true
     */
    private boolean leftEqual;
    /**
     * 非必填，结束时间（endDate）：默认不包含结束时间
     */
    private Date endTime;
    /**
     * 非必填，右包含（rightEqual）：默认为 false
     */
    private boolean rightEqual;
    /**
     * 必填，查询结果集
     */
    private List<Field> fields;
    /**
     * 非必填，查询条件
     */
    private List<Tag> tags;
    /**
     * 非必填，查询条件连接器（and、or）,默认为 and
     */
    private String tagConnector;
    /**
     * 非必填，排序(sort)：默认true(正序)
     */
    private boolean sort;
    /**
     * 非必填，数据单位（TimeUnit）:默认TimeUnit.MICROSECONDS(微秒）
     */
    private TimeUnit timeUnit;
    /**
     * 非必填，返回数据时间：默认格式为yyyy-MM-dd HH:mm:ss,可自定义时间格式
     */
    private String dataTimePattern;
    /**
     * 非必填，数据条数（每页条数）限制：默认50万条。
     */
    private int limit;
    /**
     * 当前页,默认从1开始
     */
    private Integer current;
    public QueryWrapper() {
        this.leftEqual = YacCostant.BOOLEAN_TRUE;
        this.rightEqual = YacCostant.BOOLEAN_FALSE;
        this.fields = new ArrayList<>();
        this.fields.add(new Field("时间","time","time",null,YacCostant.DECIMAL_SCALE));
        this.tags = new ArrayList<>();
        this.tagConnector = YacCostant.INFLUX_CONNECTOR_AND;
        this.sort = YacCostant.BOOLEAN_TRUE;
        this.timeUnit = TimeUnit.MICROSECONDS;
        this.dataTimePattern = YacCostant.FMT_SEC_STD;
        this.limit = YacCostant.INFLUX_MAX_SIZE;
        this.current = YacCostant.CURRENT_ONE;
    }

    public void addField(Field field){
        this.fields.add(field);
    }
    public void addTag(String name,Object value){
        this.tags.add(new Tag(name,value));
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isLeftEqual() {
        return leftEqual;
    }

    public void setLeftEqual(boolean leftEqual) {
        this.leftEqual = leftEqual;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isRightEqual() {
        return rightEqual;
    }

    public void setRightEqual(boolean rightEqual) {
        this.rightEqual = rightEqual;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTagConnector() {
        return tagConnector;
    }

    public void setTagConnector(String tagConnector) {
        this.tagConnector = tagConnector;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getDataTimePattern() {
        return dataTimePattern;
    }

    public void setDataTimePattern(String dataTimePattern) {
        this.dataTimePattern = dataTimePattern;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}
