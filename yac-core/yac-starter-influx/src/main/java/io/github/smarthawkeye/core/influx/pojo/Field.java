package io.github.smarthawkeye.core.influx.pojo;

import io.github.smarthawkeye.core.influx.common.DataType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@NoArgsConstructor
public class Field {
    /**
     * 中文名称
     */
    private String chineseName;
    /**
     * 查询结果名称（列名或聚合查询）
     */
    private String queryName;
    /**
     * 对应指标名称
     */
    private String asname;
    /**
     * 单位
     */
    private String unit;
    /**
     * 数据类型
     */
    private DataType dataType;
    /**
     * 非必填，小数位数：默认保留两位小数。
     */
    private Integer scale;

    public Field(String chineseName, String queryName, String asname, String unit, Integer scale) {
        this.chineseName = chineseName;
        this.queryName = queryName;
        this.asname = asname;
        this.unit = unit;
        this.scale = scale;
    }
    public Field(String chineseName, String queryName, String asname, String unit,DataType dataType, Integer scale) {
        this.chineseName = chineseName;
        this.queryName = queryName;
        this.asname = asname;
        this.unit = unit;
        this.scale = scale;
        this.dataType = dataType;
    }
}
