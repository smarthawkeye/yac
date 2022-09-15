package io.github.smarthawkeye.core.swagger.core;

import lombok.Data;

/**
 * @ClassName GroupInfo
 * @Author xiaoya1
 * @Link https://github.com/an0701/ya-java
 * @Date 2022/8/25 8:43
 * @Version V0.1.0
 */
@Data
public class GroupInfo {
    private String groupName;
    private String host;
    private String basepackage;
    private String title;
    private String description;
    private String version;
    private String author;
    private String authorUrl;
    private String authorEmail;
}
