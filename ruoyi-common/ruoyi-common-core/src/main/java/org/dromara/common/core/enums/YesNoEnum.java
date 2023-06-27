package org.dromara.common.core.enums;

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
    YES("Y", 1),
    /**
     * 否
     */
    NO("N", 0);

    private final String codeStr;

    private final Integer codeNum;
}
