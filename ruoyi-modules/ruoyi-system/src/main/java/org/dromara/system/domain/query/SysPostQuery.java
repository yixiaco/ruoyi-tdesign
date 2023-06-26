package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 岗位信息查询对象 sys_post
 *
 * @author hexm
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPostQuery extends BaseEntity {

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 状态（1正常 0停用）
     */
    private String status;

}
