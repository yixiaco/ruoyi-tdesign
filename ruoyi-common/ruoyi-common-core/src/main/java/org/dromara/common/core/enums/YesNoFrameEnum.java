package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单是否外链
 *
 * @author hexm
 * @date 2024/02/02 16:37
 */
@Getter
@AllArgsConstructor
public enum YesNoFrameEnum {
    /** 是否菜单外链（是） */
    YES(1),
    /** 是否菜单外链（否） */
    NO(0);

    private final Integer code;
}
