package io.github.smarthawkeye.core.influx.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据指标
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quota {
    /**
     * 指标名称
     */
    private String name;
    /**
     * 指标标签
     */
    private String tag;
    /**
     * 指标单位
     */
    private String unit;
    public Quota(Field field){
        this.name = field.getChineseName();
        this.tag = field.getAsname();
        this.unit = field.getUnit();
    }
}
