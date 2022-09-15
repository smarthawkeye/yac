package io.github.smarthawkeye.core.influx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * influx 查询条件tag
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    /**
     * tag列名
     */
    private String name;
    /**
     * tag值
     */
    private Object value;
}
