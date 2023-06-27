package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 显隐状态
 *
 * @author hexm
 * @date 2023/06/26 17:16
 */
@Getter
@AllArgsConstructor
public enum ShowHiddenEnum {

    /**
     * 显示
     */
    SHOW("1"),
    /**
     * 隐藏
     */
    HIDDEN("0");

    private final String code;
}
