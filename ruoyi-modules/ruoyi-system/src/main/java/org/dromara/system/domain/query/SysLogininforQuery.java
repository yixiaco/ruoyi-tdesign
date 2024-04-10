package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 系统访问记录查询对象 sys_logininfor
 *
 * @author hexm
 * @date 2023-09-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogininforQuery extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录状态（1成功 0失败）
     */
    private String status;

    /**
     * 客户端
     */
    private String clientKey;

    /**
     * 设备类型
     */
    private String deviceType;

}
