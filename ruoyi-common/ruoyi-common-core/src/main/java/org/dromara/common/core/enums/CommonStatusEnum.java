package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统状态(成功失败)
 *
 * @author hexm
 * @date 2023/06/26 16:36
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

    /**
     * 成功
     */
    SUCCESS("1", 1),
    /**
     * 失败
     */
    FAIL("0", 0);

    private final String code;

    private final Integer codeNum;
}
