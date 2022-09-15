package io.github.smarthawkeye.core.celldb.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Builder
@AllArgsConstructor
public class Result {
    /**
     * 结果数据，示例：[[指标1数值，指标2数值]，[指标1数值，指标2数值]......
     */
    List<List<Object>> datas;
    public Result(){
        datas = new ArrayList<>();
    }
}
