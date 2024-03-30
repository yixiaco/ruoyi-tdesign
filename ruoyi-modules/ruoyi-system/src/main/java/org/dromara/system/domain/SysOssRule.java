package org.dromara.system.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.annotation.ColumnInsert;
import org.dromara.common.mybatis.annotation.ColumnInsertOrUpdate;
import org.dromara.common.mybatis.enums.DateType;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * OSS处理规则对象 sys_oss_rule
 *
 * @author hexm
 * @date 2023-05-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_oss_rule")
public class SysOssRule extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * oss规则id
     */
    @Id
    private Long ossRuleId;

    /**
     * 租户编号
     */
    private String tenantId;

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
     * 规则
     */
    private String rule;

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

    /**
     * 创建部门
     */
    @ColumnInsert(dateType = DateType.DEPT_ID)
    private Long createDept;

    /**
     * 创建者
     */
    @ColumnInsert(dateType = DateType.USER_ID)
    private Long createBy;

    /**
     * 创建时间
     */
    @ColumnInsert(dateType = DateType.DATE)
    private Date createTime;

    /**
     * 更新者
     */
    @ColumnInsertOrUpdate(dateType = DateType.USER_ID)
    private Long updateBy;

    /**
     * 更新时间
     */
    @ColumnInsertOrUpdate(dateType = DateType.DATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
