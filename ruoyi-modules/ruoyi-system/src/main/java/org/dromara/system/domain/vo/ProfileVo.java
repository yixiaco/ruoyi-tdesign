package org.dromara.system.domain.vo;

import lombok.Data;
import org.dromara.common.sensitive.annotation.SensitiveIgnore;

/**
 * 用户个人信息
 *
 * @author Michelle.Chung
 */
@Data
public class ProfileVo {

    /**
     * 用户信息
     */
    @SensitiveIgnore
    private SysUserVo user;

    /**
     * 用户所属角色组
     */
    private String roleGroup;

    /**
     * 用户所属岗位组
     */
    private String postGroup;

    /**
     * 部门
     */
    private String deptGroup;

}
