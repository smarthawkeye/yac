package io.github.smarthawkeye.core.celldb.core;

import cn.hutool.core.util.ObjectUtil;
import io.github.smarthawkeye.core.celldb.common.CellCache;
import io.github.smarthawkeye.core.celldb.common.ResultCode;
import io.github.smarthawkeye.core.celldb.pojo.GridInfo;
import io.github.smarthawkeye.core.celldb.pojo.Meta;
import io.github.smarthawkeye.core.celldb.pojo.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@Slf4j
public class CellDBService {
    private CellDBTemplate template;
    CellCache cache;

    public CellDBService(String metaDir, String dataDir) {
        cache = new CellCache();
        template = new CellDBTemplate(metaDir, dataDir);
        init();
    }

    private void init() {
        List<Meta> metas = template.dbLoad();
        for (Meta meta : metas) {
            cache.putMeta(meta.getDb(), meta);
        }
        cache.countInit(template.getDbCount());
    }

    /**
     * 数据库创建
     */
    public int dbCreate(String db, String user, String password) {
        Meta meta = cache.getMeta(db);
        if (ObjectUtil.isNull(meta)) {
            int dbcount = cache.getCount();
            meta = Meta.builder()
                    .db(db)
                    .user(user)
                    .password(password)
                    .index(dbcount)
                    .build();
            cache.putMeta(db,meta);
            return template.dbCreate(dbcount, db, user, password);
        }
        return ResultCode.DB_CREATE_FAILED;
    }
    /**
     * 查询所有数据库信息
     */
    public List<Meta> dbList() {
        return cache.dbList();
    }
    /**
     * 仿真文件创建
     */
    public int dataFileCreate(String db, GridInfo grid) {
        GridInfo gridInfo = template.dataFileRead(db, grid.getIndex());
        if (ObjectUtil.isNotNull(gridInfo)) {
            return ResultCode.DATA_FILE_CREATE_FAILED;
        }
        template.dataFileCreate(db, grid);
        return ResultCode.SUCCESS;
    }

    /**
     * 网格数据文件查询
     * @param db    数据库名称
     * @param index 数据文件索引
     */
    public GridInfo dataFileRead(String db, int index) {
        return template.dataFileRead(db, index);
    }

    /**
     * 数据写入
     */
    public int dataWrite(String db, Integer index, Integer group, List<List<Object>> values) {
        return template.dataWrite(db, index, group, values);
    }

    /**
     * 查询一组（帧）所有网格数据
     *
     * @param db    数据库名称
     * @param index 数据文件索引
     * @param group 网格分组开
     */
    public Result dataRead(String db, Integer index, Integer group) {
        return template.dataRead(db, index, group);
    }

    /**
     * 根据网格ID查询多组数据
     * @param db         数据库名称
     * @param index      数据文件索引
     * @param cell       网格序号
     * @param groupStart 网格分组开始编号（从1开始）
     * @param groupEnd   网格分组结束编号（不大于groups）
     */
    public Result dataRead(String db, Integer index, Integer cell, Integer groupStart, Integer groupEnd) {
        return template.dataRead(db, index, cell, groupStart, groupEnd);
    }

    /**
     *
     */
    public GridInfo gridInfo(String db,Integer index) {
        return template.dataFileRead(db, index);
    }
}
