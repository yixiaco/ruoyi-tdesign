package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.system.domain.SysOssRule;

import java.io.Serial;
import java.io.Serializable;

/**
 * OSS处理规则业务对象 sys_oss_rule
 *
 * @author hexm
 * @date 2023-05-05
 */
@Data
@AutoMapper(target = SysOssRule.class, reverseConvertGenerate = false)
public class SysOssRuleBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * oss规则id
     */
    @NotNull(message = "oss规则id不能为空", groups = {EditGroup.class})
    private Long ossRuleId;

    /**
     * 规则名称（例如：80x80，则字段名称将输出字段名_80x80）
     */
    @NotBlank(message = "规则名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String ruleName;

    /**
     * 匹配域名
     */
    @NotBlank(message = "匹配域名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String domain;

    /**
     * 媒体类型（规则对匹配的媒体类型生效）
     */
    @NotBlank(message = "媒体类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String mimeType;

    /**
     * 规则
     */
    @NotBlank(message = "规则不能为空", groups = {AddGroup.class, EditGroup.class})
    private String rule;

    /**
     * 是否覆盖默认字段值
     */
    @NotBlank(message = "是否覆盖默认字段值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String isOverwrite;

    /**
     * 是否默认（不指定规则时，默认输出的规则）
     */
    @NotBlank(message = "是否默认不能为空", groups = {AddGroup.class, EditGroup.class})
    private String isDefault;

    /**
     * 启用状态
     */
    @NotBlank(message = "启用状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String status;

    /**
     * 规则顺序
     */
    @NotNull(message = "规则顺序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer ruleSort;

    /**
     * 备注
     */
    private String remark;

}
