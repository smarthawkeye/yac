package io.github.smarthawkeye.core.influx.config;


import io.github.smarthawkeye.core.influx.core.InfluxService;
import io.github.smarthawkeye.core.influx.core.InfluxTemplate;
import io.github.smarthawkeye.core.influx.props.InfluxProperties;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(InfluxProperties.class)
//@ConditionalOnProperty(value = InfluxProperties.PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
public class InfluxConfiguration {
    @Autowired
    @SuppressWarnings("all")
    private InfluxProperties properties;
    /**
     * influx读取实例
     */
    @Bean("influxDBR")
    @ConditionalOnMissingBean(InfluxProperties.class)
    public InfluxDB influxDBR(){
        return InfluxDBFactory
                .connect(properties.getReadUrl(), properties.getUsername(), properties.getPassword())
                .setDatabase(properties.getDatabase())
                .enableGzip()
                .enableBatch();
    }

    /**
     * influx增删改实例
     */
    @Bean("influxDBW")
    @ConditionalOnMissingBean(InfluxProperties.class)
    public InfluxDB influxDBW(){
        return InfluxDBFactory
                .connect(properties.getWriteUrl(), properties.getUsername(), properties.getPassword())
                .setDatabase(properties.getDatabase())
                .enableGzip()
                .enableBatch();
    }
    @Bean("influxTemplate")
    public InfluxTemplate influxTemplate(){
        return new InfluxTemplate();
    }

    @Bean("influxService")
    public InfluxService influxService(){
        return new InfluxService();
    }
    private BatchPoints getBatchPoints() {
        return BatchPoints
                .database(properties.getDatabase())
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();
    }
}
