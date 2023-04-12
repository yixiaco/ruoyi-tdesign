package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 对象存储配置查询对象 sys_oss_config
 *
 * @author hexm
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOssConfigQuery extends BaseEntity {

    /**
     * 配置key
     */
    private String configKey;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 是否默认
     */
    private String status;

}
