package io.github.smarthawkeye.core.influx.common;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class YacCostant {
    // 时间格式
    public static final String FMT_SEC_STD = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_MILLI_TRIM = "yyyyMMddHHmmssSSS";
    public static final String FMT_MILLI_STD = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FMT_MICRO_STD = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String FMT_MICRO_TRIM = "yyyyMMddHHmmssSSSSSS";

    //influx
    public static final String INFLUX_VALUE_TAG = "value";
    public static final String INFLUX_TIME_TAG = "time";
    //最大查询的数据条数
    public static final int INFLUX_MAX_SIZE = 500000;
    /**
     * 小数位
     */
    public static final int DECIMAL_SCALE = 2;
    public static final int CURRENT_ONE = 1;
    //boolean 定义
    public static final boolean BOOLEAN_TRUE = true;
    public static final boolean BOOLEAN_FALSE = false;

    public static final String  INFLUX_CONNECTOR_OR = "or";
    public static final String  INFLUX_CONNECTOR_AND = "and";
    public static final String INFLUX_SORT_ASC = "ASC";
    public static final String INFLUX_SORT_DESC = "DESC";

    //Page定义
    public static final String INFLUX_TOTAL = "total";
    public static final String INFLUX_PAGECOUNT = "pageCount";
    public static final String INFLUX_CURRENT = "current";
    public static final String INFLUX_SIZE = "size";
    public static final String INFLUX_DATAS = "datas";

}
