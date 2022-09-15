package io.github.smarthawkeye.core.influx.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "yac.influx")
public class InfluxProperties {
    /**
     * 只读Url
     */
    private String readUrl;
    /**
     * 写入Url
     */
    private String writeUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 数据库
     */
    private String database;
}
