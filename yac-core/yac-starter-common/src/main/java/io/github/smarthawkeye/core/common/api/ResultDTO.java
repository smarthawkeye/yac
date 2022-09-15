package io.github.smarthawkeye.core.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@ApiModel(value = "接口统一调用参数报文")
public class ResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "加密字符串", required = true)
    private String token;

    @ApiModelProperty(value = "时间戳，精确到毫秒（例：1652764935320）", required = true)
    private long timestamp;

    @ApiModelProperty(value = "调用方编号", required = true)
    private String code;
}
