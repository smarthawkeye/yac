package io.github.smarthawkeye.core.celldb.core;

/**
 * Grid数据库操作对象工厂
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public enum CellDBFactory {
    INSTANCE;
    public static CellDBService connect(final String metaDir, final String dataDir) {
        return new CellDBService(metaDir, dataDir);
    }
}
