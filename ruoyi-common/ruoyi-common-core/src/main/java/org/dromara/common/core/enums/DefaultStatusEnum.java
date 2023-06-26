package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认状态
 *
 * @author hexm
 * @date 2023/06/26 16:36
 */
@Getter
@AllArgsConstructor
public enum DefaultStatusEnum {

    /**
     * 默认
     */
    DEFAULT("1"),
    /**
     * 非默认
     */
    NO_DEFAULT("0");

    private final String code;
}
