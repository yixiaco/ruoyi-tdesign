package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统开关（正常停用）
 *
 * @author hexm
 * @date 2023/06/26 16:32
 */
@Getter
@AllArgsConstructor
public enum NormalDisableEnum {

    /**
     * 正常
     */
    NORMAL("1", 1),
    /**
     * 停用
     */
    DISABLE("0", 0);

    private final String code;

    private final Integer codeNum;
}
