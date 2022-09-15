package io.github.smarthawkeye.core.celldb.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据文件描述信息
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridInfo {
    /**
     * 数据库index(id)
     */
    private Integer dbIndex;
    /**
     * 数据文件id（无符号整数）
     */
    private Integer index;
    /**
     * 网格数量
     */
    private Integer cells;
    /**
     * 指标数据个数（ >0 )
     */
    private Integer quotas;
    /**
     * 存储数据类型（int->1,long->2,float->3,double->4）
     */
    private Integer dataType;
    /**
     * 数据组数(例：云图帧数）
     */
    private Integer groups;
    /**
     * 第一帧数据开始时间（时间戳：微秒）
     */
    private Long startTime;
    /**
     * 数据间隔(单位：微秒）
     */
    private Long interval;
}
