package io.github.smarthawkeye.core.common.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtModelBO implements Serializable {
    private String clientId;
    private Long expriedTime;
}
