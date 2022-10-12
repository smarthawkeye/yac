import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import io.github.smarthawkeye.core.influx.common.DataType;
import io.github.smarthawkeye.core.influx.common.YacCostant;
import io.github.smarthawkeye.core.influx.core.InfluxService;
import com.yac.example.influx.InfluxApplication;
import io.github.smarthawkeye.core.influx.pojo.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName InfluxTest
 * @Description 描述
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/26 15:09
 * @Version V0.1.0
 */
@SpringBootTest(classes = InfluxApplication.class)
public class InfluxTest {
    @Resource
    InfluxService influxService;

    @Test
    void testReadSingleTimeDataModel() {
        QueryWrapper wrapper = new QueryWrapper();
        List<Tag> tags = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        Field field = new Field("温度", "wd", "wd", "℃", 2);
        fields.add(field);
        wrapper.addField(field);

        wrapper.setMeasurement("raw_2");
        wrapper.setTags(tags);
        wrapper.setLimit(10);
        //wrapper.setTimeUnit(TimeUnit.MILLISECONDS);
        wrapper.setDataTimePattern(YacCostant.FMT_MILLI_TRIM);

        SingleTimeDataModel singleTimeDataModel = influxService.getSingleTimeDataModel(wrapper);

        String s = JSONUtil.toJsonStr(singleTimeDataModel, JSONConfig.create().setIgnoreNullValue(false));
        System.out.println("singleTimeDataModel dataModel = " + s);
    }

    @Test
    void testReadTimeDataModel() {
        QueryWrapper wrapper = new QueryWrapper();
        List<Tag> tags = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        Field field = new Field("温度", "wd", "wd", "℃", 2);
        Field fieldyl = new Field("应力", "yl", "yl", "", 2);

        wrapper.addField(field);
        wrapper.addField(fieldyl);

        wrapper.addField(field);
        wrapper.addField(fieldyl);
        wrapper.setMeasurement("raw_2");
        wrapper.setTags(tags);
        wrapper.setLimit(10);
        TimeDataModel timeDataModel = influxService.getTimeDataModel(wrapper);

        String s = JSONUtil.toJsonStr(timeDataModel, JSONConfig.create().setIgnoreNullValue(false));
        System.out.println("timeDataModel dataModel = " + s);
    }


    @Test
    void testReadQuotaDataModel() {
        QueryWrapper wrapper = new QueryWrapper();
        List<Tag> tags = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        Field field = new Field("温度", "wd", "wd", "℃", 2);
        Field fieldyl = new Field("应力", "yl", "yl", "", 4);
        fields.add(field);
        fields.add(fieldyl);
        wrapper.addField(field);
        wrapper.addField(fieldyl);

        wrapper.setMeasurement("raw_2");
        wrapper.setTags(tags);
        wrapper.setLimit(10);

        QuotaDataModel quotaDataModel = influxService.getQuotaDataModel(wrapper);
        String s = JSONUtil.toJsonStr(quotaDataModel, JSONConfig.create().setIgnoreNullValue(false));
        System.out.println("quotaDataModel dataModel = " + s);
    }

    @Test
    void testReadQuotaPageDataModel() {

        QueryWrapper wrapper = new QueryWrapper();
        List<Tag> tags = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        Field field = new Field("温度", "wd", "wd", "℃", 2);
        Field fieldyl = new Field("应力", "yl", "yl", "", 4);
        fields.add(field);
        fields.add(fieldyl);
       // wrapper.setFields(fields);
        wrapper.addField(field);
        wrapper.addField(fieldyl);

        wrapper.setMeasurement("raw_2");
        wrapper.setTags(tags);
        wrapper.setLimit(30);
        wrapper.setCurrent(2);
        QuotaPageDataModel quotaPageDataModel = influxService.getQuotaPageDataModel(wrapper);
        String s = JSONUtil.toJsonStr(quotaPageDataModel, JSONConfig.create().setIgnoreNullValue(false));
        System.out.println("quotaPageDataModel dataModel = " + s);
    }
    @Test
    void testSave() throws ParseException {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.setMeasurement("raw_2");
        Field field = new Field("温度", "wd", "wd", "℃", DataType.FLOAT,2);

        wrapper.addField(field);
        wrapper.getTags().put("td","1");
        for (int i = 0; i < 1000000; i++) {
            long time = 1663050159950124L + i;
            wrapper.add(time,3.3+i);
        }

        wrapper.setTimeUnit(TimeUnit.MICROSECONDS);
        influxService.saveOrUpdate(wrapper);
    }
    @Test
    void testDelete() throws ParseException {
        DeleteWrapper wrapper = new DeleteWrapper();
        wrapper.setStartTime(1663050159950168000L);
        wrapper.setEndTime(1663050159950168000L);
        wrapper.setMeasurement("raw_2");
        wrapper.getTags().put("td", "1");
        influxService.delete(wrapper);
    }
}
