package io.github.smarthawkeye.core.job.core;

/**
 * @ClassName ScheduledStatus
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/25 17:32
 * @Version V0.1.0
 */
public enum ScheduledStatus {
    ENABLE(0, "禁用"),
    DISABLE(1, "启用");

    private int code;
    private String name;

    ScheduledStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ScheduledStatus getByCode(int code) {
        for (ScheduledStatus st : values()) {
            if (code == st.getCode()) {
                return st;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
