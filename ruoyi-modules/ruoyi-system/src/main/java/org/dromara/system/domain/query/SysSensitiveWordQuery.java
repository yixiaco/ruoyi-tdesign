package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

import org.dromara.common.mybatis.core.domain.BasePageQuery;

/**
 * 敏感词查询对象 sys_sensitive_word
 *
 * @author hexm
 * @date 2024-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSensitiveWordQuery extends BasePageQuery {

    /**
     * 敏感词
     */
    private String word;

    /**
     * 敏感词类别
     */
    private String category;

    /**
     * 启用状态
     */
    private Integer status;

}
