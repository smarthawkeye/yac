package io.github.smarthawkeye.core.celldb.core;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.smarthawkeye.core.celldb.pojo.GridInfo;
import io.github.smarthawkeye.core.celldb.pojo.Meta;
import io.github.smarthawkeye.core.celldb.pojo.Result;
import io.github.smarthawkeye.core.celldb.util.YaDataUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * CellDB 接口
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
public class CellDBTemplate {
    private String metaDir;
    private String dataDir;
    private String meta = "meta.ya";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CellDBTemplate(String metaDir, String dataDir) {
        this.metaDir = metaDir;
        this.dataDir = dataDir;
    }

    /**
     * 数据库信息加载
     * 数据库描述信息：数据库数量->4字节（int）
     * 单个数据库数据格式：数据库索引index->4字节（int，从1开始），dbname->128字节（字符串，'\0'结尾），user->128字节（字符串，'\0'结尾）,password->128字节（字符串，'\0'结尾）
     *
     * @return meta
     */
    public List<Meta> dbLoad() {
        List<Meta> metas = new ArrayList<>();
        RandomAccessFile raf = openR(this.metaDir, this.meta);
        byte[] result = new byte[0];
        int pos = 0;
        if (ObjectUtil.isNotNull(raf)) {
            try {
                int[] dbCount = this.readInts(raf, pos, 1);
                if (dbCount != null && dbCount.length > 0) {
                    int length = (4 + 128 * 3) * dbCount[0];
                    pos = 4;
                    byte[] dbBytes = this.readBytes(raf, pos, length);
                    if (dbBytes != null) {
                        for (int i = 0; i < dbCount[0]; i++) {
                            //索引编号
                            int index = YaDataUtil.bytes2Int(Arrays.copyOfRange(dbBytes, 0, 4));
                            //数据库名称
                            String db = YaDataUtil.byte2String(Arrays.copyOfRange(dbBytes, 4, 4 + 127));
                            //数据库用户名
                            String user = YaDataUtil.byte2String(Arrays.copyOfRange(dbBytes, 4 + 128, 4 + 128 + 127));
                            //数据库密码
                            String password = YaDataUtil.byte2String(Arrays.copyOfRange(dbBytes, 4 + 128 + 128, 4 + 128 + 128 + 127));
                            metas.add(Meta.builder()
                                    .index(index)
                                    .db(db)
                                    .user(user)
                                    .password(password)
                                    .build());
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.close(raf);
            }
        }
        return metas;
    }

    /**
     * 数据库数量查询
     * @return 状态
     */
    public int getDbCount() {
        int count = 0;
        RandomAccessFile raf = openR(this.metaDir, this.meta);
        int pos = 0;
        if (ObjectUtil.isNotNull(raf)) {
            try {
                pos = 0;
                int[] dbCount = this.readInts(raf, pos, 1);
                if (dbCount != null && dbCount.length == 1) {
                    count = dbCount[0];
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.close(raf);
            }
        }
        return count;
    }

    /**
     * 数据库创建
     *
     * @param db       数据库名称
     * @param user     用户名
     * @param password 密码
     * @return 状态
     */
    public int dbCreate(int dbCount, String db, String user, String password) {
        int ret = 200;
        RandomAccessFile raf = openRW(this.metaDir, this.meta);
        int pos = 0;
        if (ObjectUtil.isNotNull(raf)) {
            try {
                //4[数据库个数] + (4[数据文件索引] + 128 * 3【dbname、user、password各占用128个字节】)
                pos = 4 + (4 + 128 * 3) * (dbCount - 1);
                //把index,db,user,password转为byte[] ,并存储到meta文件中
                byte[] bytes = new byte[4 + 128 * 3];
                //TODO
                //index转换
                byte[] indexByte = YaDataUtil.int2bytes(dbCount);
                System.arraycopy(indexByte, 0, bytes, 0, indexByte.length);
                byte[] dbBytes = YaDataUtil.string2Byte(db);
                System.arraycopy(dbBytes, 0, bytes, 4, dbBytes.length);
                byte[] userBytes = YaDataUtil.string2Byte(user);
                System.arraycopy(userBytes, 0, bytes, 132, userBytes.length);
                byte[] pwdBytes = YaDataUtil.string2Byte(password);
                System.arraycopy(pwdBytes, 0, bytes, 260, pwdBytes.length);
                ret = this.write(raf, pos, bytes);
                //更新数据库计数信息
                //int[] dbCount = this.readInts(raf, pos, 1);
                pos = 0;


                System.out.println(HexUtil.encodeHexStr(YaDataUtil.int2bytes(dbCount)));
                ret = this.writeInts(raf, pos, new int[]{dbCount});
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.close(raf);
            }
        }
        return ret;
    }


    /**
     * 数据库信息更新
     *
     * @param db       数据库名称
     * @param user     用户名
     * @param password 密码
     * @return 状态
     */
    public int dbUpdate(String db, String user, String password) {
        return 200;
    }

    /**
     * 网格数据文件创建
     *
     * @param db   数据库名称
     * @param grid 数据文件描述
     */
    public int dataFileCreate(String db, GridInfo grid) {
        //128->每一级目录最多128个，每个数据库数据文件下设置2级数据文件子目录
        int num = grid.getIndex() % 128;
        String path = YaDataUtil.stringConcat("/", this.dataDir, db, String.valueOf(num), String.valueOf(num));
        RandomAccessFile raf = this.openRW(path, grid.getIndex() + ".yad");
        if (ObjectUtil.isNotNull(raf)) {
            try {
                long pos = 0;
                int[] ivals = new int[4];
                ivals[0] = grid.getCells();
                ivals[1] = grid.getQuotas();
                ivals[2] = grid.getDataType();
                ivals[3] = grid.getGroups();
                this.writeInts(raf, pos, ivals);
                pos += 16;
                long[] lvals = new long[2];
                lvals[0] = grid.getStartTime();
                lvals[1] = grid.getInterval();
                this.writeLongs(raf, pos, lvals);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.close(raf);
            }
        }
        return 200;
    }

    /**
     * 网格数据文件查询
     *
     * @param db    数据库名称
     * @param index 数据文件索引
     * @return GridInfo
     */
    public GridInfo dataFileRead(String db, int index) {
        //128->每一级目录最多128个，每个数据库数据文件下设置2级数据文件子目录
        int num = index % 128;
        String path = YaDataUtil.stringConcat("/", this.dataDir, db, String.valueOf(num), String.valueOf(num));
        RandomAccessFile raf = this.openR(path, index + ".yad");
        GridInfo grid = null;
        if (ObjectUtil.isNotNull(raf)) {
            try {
                grid = new GridInfo();
                grid.setIndex(index);
                long pos = 0;
                int[] ivals = this.readInts(raf, pos, 4);
                if (ivals != null) {
                    grid.setCells(ivals[0]);
                    grid.setQuotas(ivals[1]);
                    grid.setDataType(ivals[2]);
                    grid.setGroups(ivals[3]);
                }
                pos += 16;
                long[] lvals = this.readLongs(raf, pos, 2);
                if (lvals != null) {
                    grid.setStartTime(lvals[0]);
                    grid.setInterval(lvals[1]);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                this.close(raf);
            }
        }
        return grid;
    }

    /**
     * 网格数据写入
     *
     * @param db     数据库名称
     * @param index  数据文件索引
     * @param group 组
     * @param values 值
     * @return 状态
     */
    public int dataWrite(String db, Integer index, Integer group, List<List<Object>> values) {
        //128->每一级目录最多128个，每个数据库数据文件下设置2级数据文件子目录
        int num = index % 128;
        String path = YaDataUtil.stringConcat("/", this.dataDir, db, String.valueOf(num), String.valueOf(num));
        GridInfo grid = dataFileRead(db, index);
        if (ObjectUtil.isNotNull(grid) && grid.getGroups() >= group && group > 0 && grid.getCells() == values.size()) {
            RandomAccessFile raf = this.openRW(path, index + ".yad");
            if (ObjectUtil.isNotNull(raf)) {
                try {
                    // 存储数据类型（int->1,long->2,float->3,double->4）
                    int idx = 0;
                    long pos = 0;
                    int dataCount = grid.getCells() * grid.getQuotas();
                    switch (grid.getDataType()) {
                        case 1://int->1
                            byte[] ibytes = new byte[dataCount * 4];
                            idx = 0;
                            for (int i = 0; i < grid.getCells(); i++) {
                                for (int j = 0; j < grid.getQuotas(); j++) {
                                    byte[] bvals = YaDataUtil.int2bytes(Integer.valueOf(values.get(i).get(j).toString()));
                                    System.arraycopy(bvals, 0, ibytes, idx, 4);
                                    idx += 4;
                                }
                            }
                            //32位描述信息，（网格数 * 指标数 *数据所占字节数）*（第几组-1）
                            pos = 1L * 32 + dataCount * 4 * (group - 1);
                            this.write(raf, pos, ibytes);
                            break;
                        case 2://long->2
                            byte[] lbytes = new byte[dataCount * 8];
                            idx = 0;
                            for (int i = 0; i < grid.getCells(); i++) {
                                for (int j = 0; j < grid.getQuotas(); j++) {
                                    byte[] bvals = YaDataUtil.long2bytes(Long.valueOf(values.get(i).get(j).toString()));
                                    System.arraycopy(bvals, 0, lbytes, idx, 8);
                                    idx += 8;
                                }
                            }
                            pos = 1L * 32 + dataCount * 8 * (group - 1);
                            this.write(raf, pos, lbytes);
                            break;
                        case 3://float->3

                            byte[] fbytes = new byte[dataCount * 4];
                            idx = 0;

                            for (int i = 0; i < grid.getCells(); i++) {
                                for (int j = 0; j < grid.getQuotas(); j++) {
                                    byte[] bvals = YaDataUtil.float2bytes(Float.valueOf(values.get(i).get(j).toString()));
                                    System.arraycopy(bvals, 0, fbytes, idx, 4);
                                    idx += 4;
                                }
                            }
                            pos = 1L * 32 + dataCount * 4 * (group - 1);
                            this.write(raf, pos, fbytes);
                            break;
                        case 4://double->4

                            byte[] dbytes = new byte[dataCount * 8];
                            idx = 0;
                            for (int i = 0; i < grid.getCells(); i++) {
                                for (int j = 0; j < grid.getQuotas(); j++) {
                                    byte[] dvals = YaDataUtil.double2bytes(Double.valueOf(values.get(i).get(j).toString()));
                                    System.arraycopy(dvals, 0, dbytes, idx, 8);
                                    idx += 8;
                                }
                            }
                            pos = 1L * 32 + dataCount * 8 * (group - 1);
                            this.write(raf, pos, dbytes);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    this.close(raf);
                }
            }
        }
        return 200;
    }

    /**
     * 根据网格ID查询多组数据
     * 结果数据，示例：[[时间，指标1数值，指标2数值]，[时间，指标1数值，指标2数值]......
     *
     * @param db         数据库名称
     * @param index      数据文件索引
     * @param cell       网格序号
     * @param groupStart 网格分组开始编号（从1开始）
     * @param groupEnd   网格分组结束编号（不大于groups）
     * @return Result
     */
    public Result dataRead(String db, Integer index, Integer cell, Integer groupStart, Integer groupEnd) {
        Result result = new Result();
        //128->每一级目录最多128个，每个数据库数据文件下设置2级数据文件子目录
        int num = index % 128;
        String path = YaDataUtil.stringConcat("/", this.dataDir, db, String.valueOf(num), String.valueOf(num));
        GridInfo grid = dataFileRead(db, index);
        if (ObjectUtil.isNotNull(grid) && grid.getGroups() >= groupStart) {
            RandomAccessFile raf = this.openR(path, index + ".yad");
            int dataCount = grid.getCells() * grid.getQuotas();
            int group = groupEnd - groupStart + 1;
            if (ObjectUtil.isNotNull(raf)) {
                try {
                    // 存储数据类型（int->1,long->2,float->3,double->4）
                    int idx = 0;
                    long pos = 1L * 32;
                    //grid.getInterval()为微秒，转为毫秒
                    long interval = grid.getInterval() / 1000;
                    long dataTime = grid.getStartTime() / 1000 + interval * (groupStart - 1);
                    switch (grid.getDataType()) {
                        case 1://int->1
                            //第一组开始索引
                            pos += 1L * grid.getCells() * grid.getQuotas() * 4 * (groupStart - 1) + (cell - 1) * grid.getQuotas() * 4;
                            for (int k = 0; k < group; k++) {
                                //
                                List<Object> objects = new ArrayList<>();
                                int[] ivals = this.readInts(raf, pos, grid.getQuotas());
                                if (ivals == null) {
                                    break;
                                }
                                objects.add(sdf.format(new Date(dataTime)));
                                dataTime += interval;
                                for (int ival : ivals) {
                                    objects.add(ival);
                                }
                                result.getDatas().add(objects);
                                pos += grid.getCells() * grid.getQuotas() * 4;
                            }
                            break;
                        case 2://long->2
                            //第一组开始索引
                            pos += 1L * grid.getCells() * grid.getQuotas() * 8 * (groupStart - 1) + (cell - 1) * grid.getQuotas() * 8;
                            for (int k = 0; k < group; k++) {
                                //
                                List<Object> objects = new ArrayList<>();
                                long[] lvals = this.readLongs(raf, pos, grid.getQuotas());
                                if (lvals == null) {
                                    break;
                                }
                                objects.add(sdf.format(new Date(dataTime)));
                                dataTime += interval;
                                for (long lval : lvals) {
                                    objects.add(lval);
                                }
                                result.getDatas().add(objects);
                                pos += grid.getCells() * grid.getQuotas() * 8;

                            }
                            break;
                        case 3://float->3
                            //第一组开始索引
                            pos += 1L * grid.getCells() * grid.getQuotas() * 4 * (groupStart - 1) + (cell - 1) * grid.getQuotas() * 4;
                            for (int k = 0; k < group; k++) {
                                //
                                List<Object> objects = new ArrayList<>();
                                float[] fvals = this.readFloats(raf, pos, grid.getQuotas());
                                if (fvals == null) {
                                    break;
                                }
                                objects.add(sdf.format(new Date(dataTime)));
                                dataTime += interval;
                                for (float fval : fvals) {
                                    objects.add(fval);
                                }
                                result.getDatas().add(objects);
                                pos += grid.getCells() * grid.getQuotas() * 4;

                            }
                            break;
                        case 4://double->4
                            //第一组开始索引
                            pos += 1L * grid.getCells() * grid.getQuotas() * 8 * (groupStart - 1) + (cell - 1) * grid.getQuotas() * 8;
                            for (int k = 0; k < group; k++) {
                                //
                                List<Object> objects = new ArrayList<>();
                                double[] dvals = this.readDoubles(raf, pos, grid.getQuotas());
                                if (dvals == null) {
                                    break;
                                }
                                objects.add(sdf.format(new Date(dataTime)));
                                dataTime += interval;
                                for (double dval : dvals) {
                                    objects.add(dval);
                                }
                                result.getDatas().add(objects);
                                pos += grid.getCells() * grid.getQuotas() * 8;
                            }
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    this.close(raf);
                }
            }
        }
        return result;
    }

    /**
     * 查询一组（帧）所有网格数据
     *
     * @param db    数据库名称
     * @param index 数据文件索引
     * @param group 网格分组开
     * @return Result
     */
    public Result dataRead(String db, Integer index, Integer group) {
        Result result = new Result();
        //128->每一级目录最多128个，每个数据库数据文件下设置2级数据文件子目录
        int num = index % 128;
        String path = YaDataUtil.stringConcat("/", this.dataDir, db, String.valueOf(num), String.valueOf(num));
        GridInfo grid = dataFileRead(db, index);
        if (ObjectUtil.isNotNull(grid) && (grid.getGroups() >= group && group > 0)) {
            RandomAccessFile raf = this.openR(path, index + ".yad");
            int dataCount = grid.getCells() * grid.getQuotas();
            if (ObjectUtil.isNotNull(raf)) {
                try {
                    // 存储数据类型（int->1,long->2,float->3,double->4）
                    int idx = 0;
                    long pos = 1L * 32;
                    switch (grid.getDataType()) {
                        case 1://int->1
                            pos += 1L * grid.getCells() * grid.getQuotas() * 4 * (group - 1);
                            for (int i = 0; i < grid.getCells(); i++) {
                                int[] ival = this.readInts(raf, pos, grid.getQuotas());
                                if (ival == null) {
                                    break;
                                }
                                List<Object> objects = new ArrayList<>();
                                for (int j = 0; j < ival.length; j++) {
                                    objects.add(ival[j]);
                                }
                                result.getDatas().add(objects);
                                pos += 4 * grid.getQuotas();
                            }
                            break;
                        case 2://long->2
                            pos += 1L * grid.getCells() * grid.getQuotas() * 8 * (group - 1);
                            for (int i = 0; i < grid.getCells(); i++) {
                                long[] lval = this.readLongs(raf, pos, grid.getQuotas());
                                if (lval == null) {
                                    break;
                                }
                                List<Object> objects = new ArrayList<>();
                                for (int j = 0; j < lval.length; j++) {
                                    objects.add(lval[j]);
                                }
                                result.getDatas().add(objects);
                                pos += 8 * grid.getQuotas();
                            }
                            break;
                        case 3://float->3
                            pos += 1L * grid.getCells() * grid.getQuotas() * 4 * (group - 1);
                            for (int i = 0; i < grid.getCells(); i++) {
                                float[] fval = this.readFloats(raf, pos, grid.getQuotas());
                                if (fval == null) {
                                    break;
                                }
                                List<Object> objects = new ArrayList<>();
                                for (int j = 0; j < fval.length; j++) {
                                    objects.add(fval[j]);
                                }
                                result.getDatas().add(objects);
                                pos += 4 * grid.getQuotas();
                            }
                            break;
                        case 4://double->4
                            pos += 1L * grid.getCells() * grid.getQuotas() * 8 * (group - 1);
                            for (int i = 0; i < grid.getCells(); i++) {
                                double[] dval = this.readDoubles(raf, pos, grid.getQuotas());
                                if (dval == null) {
                                    break;
                                }
                                List<Object> objects = new ArrayList<>();
                                for (int j = 0; j < dval.length; j++) {
                                    objects.add(dval[j]);
                                }
                                result.getDatas().add(objects);
                                pos += 8 * grid.getQuotas();
                            }
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    this.close(raf);
                }
            }
        }
        return result;
    }

    /**
     * byte数据文件读取
     *
     * @param pos    下标
     * @param length 长度
     * @return byte[]
     */
    private byte[] readBytes(RandomAccessFile raf, long pos, int length) {
        byte[] result = new byte[length];
        try {
            raf.seek(pos);
            raf.read(result, 0, length);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * float数据读取
     *
     * @param raf
     * @param pos   下标
     * @param count 读取数据个数
     */
    private float[] readFloats(RandomAccessFile raf, long pos, int count) {
        float[] result = new float[count];
        if (ObjectUtil.isNotNull(raf)) {
            try {
                byte[] bytes = readBytes(raf, pos, 4 * count);
                for (int i = 0; i < count; i++) {
                    byte[] fbytes = new byte[4];
                    fbytes[0] = bytes[i * 4];
                    fbytes[1] = bytes[i * 4 + 1];
                    fbytes[2] = bytes[i * 4 + 2];
                    fbytes[3] = bytes[i * 4 + 3];
                    result[i] = YaDataUtil.bytes2Float(fbytes);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return result;
    }

    /**
     * int数据读取
     *
     * @param pos   下标
     * @param count 读取数据个数
     * @return int[]
     */
    private int[] readInts(RandomAccessFile raf, long pos, int count) {
        int[] result = new int[count];
        try {
            byte[] bytes = readBytes(raf, pos, 4 * count);
            for (int i = 0; i < count; i++) {
                byte[] fbytes = new byte[4];
                fbytes[0] = bytes[i * 4];
                fbytes[1] = bytes[i * 4 + 1];
                fbytes[2] = bytes[i * 4 + 2];
                fbytes[3] = bytes[i * 4 + 3];
                result[i] = YaDataUtil.bytes2Int(fbytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * long数据读取
     *
     * @param pos   下标
     * @param count 读取数据个数
     * @return long[]
     */
    private long[] readLongs(RandomAccessFile raf, long pos, int count) {
        long[] result = new long[count];
        try {
            byte[] bytes = readBytes(raf, pos, 8 * count);
            for (int i = 0; i < count; i++) {
                byte[] fbytes = new byte[8];
                fbytes[0] = bytes[i * 8];
                fbytes[1] = bytes[i * 8 + 1];
                fbytes[2] = bytes[i * 8 + 2];
                fbytes[3] = bytes[i * 8 + 3];
                fbytes[4] = bytes[i * 8 + 4];
                fbytes[5] = bytes[i * 8 + 5];
                fbytes[6] = bytes[i * 8 + 6];
                fbytes[7] = bytes[i * 8 + 7];
                result[i] = YaDataUtil.bytes2Long(fbytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * double数据读取
     *
     * @param pos   下标
     * @param count 读取数据个数
     * @return double[]
     */
    private double[] readDoubles(RandomAccessFile raf, long pos, int count) {
        double[] result = new double[count];
        if (ObjectUtil.isNotNull(raf)) {
            try {
                byte[] bytes = readBytes(raf, pos, 8 * count);
                for (int i = 0; i < count; i++) {
                    byte[] fbytes = new byte[8];
                    fbytes[0] = bytes[i * 8];
                    fbytes[1] = bytes[i * 8 + 1];
                    fbytes[2] = bytes[i * 8 + 2];
                    fbytes[3] = bytes[i * 8 + 3];
                    fbytes[4] = bytes[i * 8 + 4];
                    fbytes[5] = bytes[i * 8 + 5];
                    fbytes[6] = bytes[i * 8 + 6];
                    fbytes[7] = bytes[i * 8 + 7];
                    result[i] = YaDataUtil.bytes2Double(fbytes);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return result;
    }

    /**
     * 写文件
     *
     * @param pos   下标
     * @param bytes 数据
     * @return 状态
     */
    private int write(RandomAccessFile raf, long pos, byte[] bytes) {
        if (ObjectUtil.isNotNull(raf)) {
            try {
                raf.seek(pos);
                raf.write(bytes);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * float数据写入
     *
     * @param raf
     * @param pos    下标
     * @param values 数据
     * @return 状态
     */
    private int writeFloats(RandomAccessFile raf, long pos, float[] values) {
        if (ObjectUtil.isNotNull(raf)) {
            try {
                if (ObjectUtil.isNotNull(values)) {
                    this.write(raf,pos,YaDataUtil.floats2Bytes(values));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * int数据写入
     *
     * @param raf
     * @param pos    下标
     * @param values 数据
     * @return 状态
     */
    private int writeInts(RandomAccessFile raf, long pos, int[] values) {
        if (ObjectUtil.isNotNull(raf)) {
            try {
                if (ObjectUtil.isNotNull(values)) {
                    this.write(raf,pos,YaDataUtil.ints2Bytes(values));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * long数据写入
     *
     * @param raf
     * @param pos    下标
     * @param values 数据
     * @return 状态
     */
    private int writeLongs(RandomAccessFile raf, long pos, long[] values) {
        if (ObjectUtil.isNotNull(raf)) {
            try {
                if (ObjectUtil.isNotNull(values)) {
                    this.write(raf,pos,YaDataUtil.longs2Bytes(values));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * double数据写入
     *
     * @param raf
     * @param pos    下标
     * @param values 数据
     * @return 状态
     */
    private int writeDoubles(RandomAccessFile raf, long pos, double[] values) {
        if (ObjectUtil.isNotNull(raf)) {
            try {
                if (ObjectUtil.isNotNull(values)) {
                    this.write(raf,pos,YaDataUtil.doubles2Bytes(values));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 打开文件
     *
     * @param path     文件路径
     * @param fileName 文件名称
     * @return RandomAccessFile
     */
    private RandomAccessFile openRW(String path, String fileName) {
        RandomAccessFile raf = null;
        try {
            File file = new File(path);
            if (!file.exists()) { //如果不存在
                boolean dr = file.mkdirs(); //创建目录
            }
            raf = new RandomAccessFile(path + "/" + fileName, "rw");
        } catch (Exception ex) {

        }
        return raf;
    }

    /**
     * 打开文件
     *
     * @param path     文件路径
     * @param fileName 文件名称
     * @return RandomAccessFile
     */
    private RandomAccessFile openR(String path, String fileName) {
        RandomAccessFile raf = null;
        try {
            File file = new File(path);
            if (!file.exists()) { //如果不存在
                return null;
            }
            raf = new RandomAccessFile(path + "/" + fileName, "r");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return raf;
    }

    /**
     * 关闭文件
     */
    private void close(RandomAccessFile raf) {
        if (ObjectUtil.isNotNull(raf)) {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
