package io.github.smarthawkeye.core.common.pojo.dto;

import io.github.smarthawkeye.core.common.api.ResultDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@ApiModel(value = "Oss接口统一调用参数报文")
public class OssResultDTO extends ResultDTO {
    private static final long serialVersionUID = -3076427280459858593L;
//    @ApiModelProperty(value = "数据参数")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private T data;

}
