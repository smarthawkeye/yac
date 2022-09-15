package io.github.smarthawkeye.core.influx.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UpdateWrapper
 * @Description 描述
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/9/13 15:16
 * @Version V0.1.0
 */
@Data
@AllArgsConstructor
@Builder
public class DeleteWrapper {
    /**
     * 必填，数据表名称
     */
    private String measurement;
    /**
     * 非必填，tags
     */
    private Map<String,String> tags;

    /**
     * 开始时间（纳秒-时间戳）
     */
    private long startTime;
    /**
     * 结束时间（纳秒-时间戳）
     */
    private long endTime;

    public DeleteWrapper() {
        this.tags = new HashMap<>();
    }
}
