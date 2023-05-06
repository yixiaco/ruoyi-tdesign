package org.dromara.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否枚举
 *
 * @author hexm
 * @date 2023/05/06 11:29
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum {

    /**
     * 是
     */
    YES("Y"),
    /**
     * 否
     */
    NO("N");

    private final String code;
}
