package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型
 *
 * @author hexm
 * @date 2024/02/02 16:31
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {
    /** 菜单类型（目录） */
    DIRECTORY("M"),
    /** 菜单类型（菜单） */
    MENU("C"),
    /** 菜单类型（按钮） */
    BUTTON("F");

    private final String type;
}
