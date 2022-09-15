/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package io.github.smarthawkeye.core.celldb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CellDB配置信息
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "yac.celldb")
public class CellDBProperties {
    /**
     * 数据库信息描述文件目录
     */
    @Value("${yac.celldb.meta.dir}")
    private String metaDir;
    /**
     * 数据文件目录
     */
    @Value("${yac.celldb.data.dir}")
    private String dataDir;

}
