package io.github.smarthawkeye.core.celldb.common;

/**
 * 状态码定义
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class ResultCode {
    /**
     * 成功
     */
    public static int SUCCESS = 200;
    /**
     * 失败
     */
    public static int FAILED = -1;
    /**
     * 数据库创建失败
     */
    public static int DB_CREATE_FAILED = -2;
    /**
     * 数据连接失败
     */
    public static int DB_CONNECT_FAILED = -3;
    /**
     * 数据查询失败
     */
    public static int DATA_QUERY_FAILED = -4;
    /**
     * 数据存储失败
     */
    public static int DATA_SAVE_FAILED = -5;
    /**
     * 数据文件创建失败
     */
    public static int DATA_FILE_CREATE_FAILED = -6;
}
