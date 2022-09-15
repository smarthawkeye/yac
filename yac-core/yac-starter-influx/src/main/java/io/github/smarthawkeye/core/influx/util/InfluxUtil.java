package io.github.smarthawkeye.core.influx.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName InfluxUtil
 * @Description 工具类
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/9/13 13:54
 * @Version V0.1.0
 */
public class InfluxUtil {
    /**
     * 时间戳转LocalDateTime
     *
     * @param time     时间戳，支持秒、毫秒、微妙、纳秒
     * @param timeUnit 时间单位，支持秒、毫秒、微妙、纳秒的时间戳
     * @return
     */
    public static LocalDateTime timeToLocalDateTime(long time, TimeUnit timeUnit) {
        Calendar calendar = Calendar.getInstance();
        int Nano = 0;
        if (timeUnit.equals(TimeUnit.NANOSECONDS)) {
            calendar.setTimeInMillis(time / 1000000);
            Nano = (int) (time % 1000000000);
        } else if (timeUnit.equals(TimeUnit.MICROSECONDS)) {
            calendar.setTimeInMillis(time / 1000);
            Nano = (int) (time % 1000000) * 1000;
        } else if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
            calendar.setTimeInMillis(time);
            Nano = (int) (time % 1000) * 1000000;
        } else if (timeUnit.equals(TimeUnit.SECONDS)) {
            calendar.setTimeInMillis(time * 1000);
            Nano = (int) (time) * 1000000000;
        }
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH) + 1;
        int DAY_OF_MONTH = calendar.get(Calendar.DAY_OF_MONTH);
        int HOUR_OF_DAY = calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar.get(Calendar.MINUTE);
        int SECOND = calendar.get(Calendar.SECOND);
        LocalDateTime ldt = LocalDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR_OF_DAY, MINUTE, SECOND, Nano);
        return ldt;
    }

    /**
     * 时间戳转ZonedDateTime
     *
     * @param time     时间戳，支持秒、毫秒、微妙、纳秒
     * @param timeUnit 时间单位，支持秒、毫秒、微妙、纳秒的时间戳
     * @return
     */
    public static ZonedDateTime timeToZonedDateTime(long time, TimeUnit timeUnit) {
        LocalDateTime ldt = timeToLocalDateTime(time, timeUnit);
        return ZonedDateTime.of(ldt, ZoneId.systemDefault());
    }

    /**
     * 时间戳转ZonedDateTime
     *
     * @param time     时间戳，支持秒、毫秒、微妙、纳秒
     * @param timeUnit 时间单位，支持秒、毫秒、微妙、纳秒的时间戳
     * @param zoneId   时区
     * @return
     */
    public static ZonedDateTime timeToZonedDateTime(long time, TimeUnit timeUnit, ZoneId zoneId) {
        LocalDateTime ldt = timeToLocalDateTime(time, timeUnit);
        return ZonedDateTime.of(ldt, zoneId);
    }

    /**
     * 时间戳转String
     * @param time     时间戳，支持秒、毫秒、微妙、纳秒
     * @param timeUnit 时间单位，支持秒、毫秒、微妙、纳秒的时间戳
     * @param zoneId   时区
     * @param pattern 时间格式
     * @return
     */
    public static String timeToString(long time, TimeUnit timeUnit, ZoneId zoneId, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(timeToZonedDateTime(time, timeUnit, zoneId));
    }

    /**
     * 时间戳转String
     * @param time     时间戳，支持秒、毫秒、微妙、纳秒
     * @param timeUnit 时间单位，支持秒、毫秒、微妙、纳秒的时间戳
     * @param pattern 时间格式(例：yyyy-MM-dd HH:mm:ss.SSSSSS)
     * @return
     */
    public static String timeToString(long time, TimeUnit timeUnit, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(timeToZonedDateTime(time, timeUnit));
    }
}
