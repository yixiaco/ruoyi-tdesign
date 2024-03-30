package org.dromara.common.mybatis.enums;

/**
 * 数据填充策略
 */
public enum DateType {
    /**
     * 不填充数据
     */
    None,
    /**
     * 时间类型，支持类型：Date、Long、LocalDateTime、LocalDate、LocalTime、Instant、Timestamp
     */
    DATE,
    /**
     * 当前用户id，支持类型: String、Long
     */
    USER_ID,
    /**
     * 用户名称，支持类型: String
     */
    USERNAME,
    /**
     * 部门id，支持类型: String、Long
     */
    DEPT_ID,
    /**
     * 当前请求IP，支持类型: String
     */
    IP,
    /**
     * ip地址，支持类型: String
     */
    IP_ADDRESS,
    /**
     * 逻辑未删除值，支持类型：String、Long、Integer、Short、Byte、Character
     */
    LOGIC_NOT_DELETE,
    /**
     * 逻辑删除值，支持类型：String、Long、Integer、Short、Byte、Character
     */
    LOGIC_DELETE,
    /**
     * 填充零，支持类型：String、Long、Integer、Short、Byte、Character
     */
    ZERO,
    /**
     * 填充一，支持类型：String、Long、Integer、Short、Byte、Character
     */
    ONE;
}
