package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 操作日志记录查询对象 sys_oper_log
 *
 * @author hexm
 * @date 2023-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOperLogQuery extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型
     */
    private Long businessType;

    /**
     * 业务类型数组
     */
    private Integer[] businessTypes;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 主机地址
     */
    private String operIp;

    /**
     * 操作状态
     */
    private Integer status;

}
