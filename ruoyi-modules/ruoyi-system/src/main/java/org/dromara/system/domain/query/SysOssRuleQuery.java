package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * OSS处理规则查询对象 sys_oss_rule
 *
 * @author hexm
 * @date 2023-05-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOssRuleQuery extends BaseEntity {

    /**
     * 规则名称（例如：80x80，则字段名称将输出字段名_80x80）
     */
    private String ruleName;

    /**
     * 匹配域名
     */
    private String domain;

    /**
     * 媒体类型（规则对匹配的媒体类型生效）
     */
    private String mimeType;

    /**
     * 是否覆盖默认字段值
     */
    private String isOverwrite;

    /**
     * 是否默认（不指定规则时，默认输出的规则）
     */
    private String isDefault;

    /**
     * 启用状态
     */
    private String status;

}
