package io.github.smarthawkeye.core.celldb.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库索引描述
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meta {
    /**
     * 数据库索引编号，创建数据库时顺序增加
     */
    private Integer index;
    /**
     * 数据库名称
     */
    private String db;
    /**
     * 数据库用户名
     */
    private String user;
    /**
     * 数据库密码
     */
    private String password;
}
