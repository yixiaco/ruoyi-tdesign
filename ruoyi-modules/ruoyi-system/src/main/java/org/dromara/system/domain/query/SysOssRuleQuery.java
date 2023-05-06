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
     * 是否默认（不使用变量名，直接替换字段内容）
     */
    private String isDefault;

    /**
     * 启用状态
     */
    private String status;

}
