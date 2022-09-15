package io.github.smarthawkeye.core.influx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 分页
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@AllArgsConstructor
public abstract class PageDataModel extends DataModel{
    /**
     * 总条数
     */
    private Integer total;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 当前页
     */
    private Integer current;
    public PageDataModel(){
        this.total = 0;
        this.pageCount = 0;
        this.current = 0;
        this.size = 0;
    }
}
