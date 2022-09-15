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

import io.github.smarthawkeye.core.celldb.core.CellDBFactory;
import io.github.smarthawkeye.core.celldb.core.CellDBService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CellDB自动配置类
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(CellDBProperties.class)
public class CellDBConfiguration {
    @Autowired
    @SuppressWarnings("all")
    private CellDBProperties properties;

    @Bean
    @ConditionalOnMissingBean(CellDBService.class)
    public CellDBService cellTemplate() {
        return CellDBFactory.connect(properties.getMetaDir(),properties.getDataDir());
    }
}
