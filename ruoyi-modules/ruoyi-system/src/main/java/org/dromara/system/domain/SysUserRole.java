package org.dromara.system.domain;

import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author Lion Li
 */

@Data
@Table("sys_user_role")
public class SysUserRole {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
