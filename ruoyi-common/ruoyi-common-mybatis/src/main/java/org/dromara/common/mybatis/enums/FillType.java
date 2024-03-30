package org.dromara.common.mybatis.enums;

/**
 * 字段填充策略
 */
public enum FillType {
    /**
     * 插入时填充字段
     */
    INSERT,
    /**
     * 更新时填充字段
     */
    UPDATE,
    /**
     * 插入和更新时填充字段
     */
    INSERT_UPDATE
}
