import cn.hutool.core.util.HexUtil;
import io.github.smarthawkeye.core.celldb.core.CellDBService;
import io.github.smarthawkeye.core.celldb.pojo.GridInfo;
import io.github.smarthawkeye.core.celldb.pojo.Meta;
import io.github.smarthawkeye.core.celldb.pojo.Result;
import io.github.smarthawkeye.core.celldb.util.YaDataUtil;
import com.yac.example.celldb.CellApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MailTest
 * @Description 描述
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/26 13:20
 * @Version V0.1.0
 */
@SpringBootTest(classes = CellApplication.class)
public class YacTest {
    @Resource
    CellDBService service;

    @Test
    public void test(){
        byte[] bytes = YaDataUtil.longs2Bytes(new long[]{1660811948000L});
        System.out.println("long = 1660811948000L");
        System.out.println(HexUtil.encodeHexStr(bytes));
        System.out.println(YaDataUtil.bytes2Long(bytes));


        byte[] bytes1 = YaDataUtil.ints2Bytes(new int[]{166727377});
        System.out.println("int = 166727377");
        System.out.println(HexUtil.encodeHexStr(bytes1));
        System.out.println(YaDataUtil.bytes2Int(bytes1));

        byte[] bytes2 = YaDataUtil.floats2Bytes(new float[]{166727.34f});
        System.out.println("float = 166727.34f");
        System.out.println(HexUtil.encodeHexStr(bytes2));
        System.out.println(YaDataUtil.bytes2Float(bytes2));

        byte[] bytes3 = YaDataUtil.doubles2Bytes(new double[]{166727.34});
        System.out.println("double = 166727.34");
        System.out.println(HexUtil.encodeHexStr(bytes3));
        System.out.println(YaDataUtil.bytes2Double(bytes3));

        List<Meta> dbList = service.dbList();
        System.out.println(dbList);
    }

    //db创建测试
    @Test
    public void dbcreate(){
        String db = "db003",user = "admin",password = "123456";
        int ret = service.dbCreate(db, user, password);
        System.out.println(ret);
    }
    //grid创建测试
    @Test
    public void gridcreate(){
        String db = "db003";
        Integer index = 1;
        Integer cells = 100;
        Integer quotas = 1;
        Integer dataType = 1;
        Integer groups = 24;
        Long startTime = 123456789L;
        Long interval = 1000L;

        GridInfo grid = GridInfo.builder()
                .index(index)
                .cells(cells)
                .quotas(quotas)
                .dataType(dataType)
                .groups(groups)
                .startTime(startTime)
                .interval(interval)
                .build();
        int ret = service.dataFileCreate(db,grid);
        System.out.println("ret = " + ret);
        GridInfo gridInfo = service.dataFileRead(db, grid.getIndex());
        System.out.println("gridInfo = " + gridInfo);
        System.out.println(ret);
    }
    //数据写入测试
    @Test
    public void datawrite(){
        String db = "db003";
        Integer index = 1;
        Integer quotas = 1;
        Integer groupStart = 1;
        Integer groupEnd = 2;

        for (int k = 0; k < groupEnd-groupStart+1; k++) {
            List<List<Object>> values = new ArrayList<>();
            GridInfo gridInfo = service.gridInfo(db,index);
            for (int i = 0; i < gridInfo.getCells(); i++) {
                List<Object> objs = new ArrayList<>();
                for (int j = 0; j < gridInfo.getQuotas(); j++) {
                    objs.add(6);
                }
                values.add(objs);
            }
            service.dataWrite(db, index, groupStart + k,values);
        }
    }

    //数据读取测试
    @Test
    public void dataread(){
        String db = "db003";
        Integer index = 1;
        Integer group = 1;
        Result result = service.dataRead(db, index, group);
        System.out.println("result = " + result.getDatas().size());
    }
    @Test
    public void gridInfo(){
        String db = "db003";
        Integer index = 1;
        GridInfo gridInfo = service.gridInfo(db,index);
        System.out.println(gridInfo);
    }
    @Test
    public void pointdata(){
        String db = "db003";
        Integer index = 1;
        Integer cell = 1;
        Integer groupStart = 1;
        Integer groupEnd = 2;
        Result result = service.dataRead(db,index,cell,groupStart,groupEnd);
    }
}
