package org.dromara.system.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.annotation.ColumnInsert;
import org.dromara.common.mybatis.annotation.ColumnInsertOrUpdate;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.enums.DateType;

import java.io.Serial;
import java.util.Date;

/**
 * 消息模板对象 sys_message_template
 *
 * @author hexm
 * @date 2023-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_message_template")
public class SysMessageTemplate extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息模板id
     */
    @Id
    private Long messageTemplateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 消息配置id
     */
    private Long messageConfigId;

    /**
     * 消息key主键
     */
    private Long messageKeyId;

    /**
     * 消息key
     */
    private String messageKey;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 模板类型
     */
    private String templateMode;

    /**
     * 标题
     */
    private String title;

    /**
     * 签名
     */
    private String signature;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 内容
     */
    private String content;

    /**
     * 输入变量
     */
    private String varsJson;

    /**
     * 状态（1正常 0停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建部门
     */
    @ColumnInsert(dateType = DateType.DEPT_ID)
    private Long createDept;

    /**
     * 更新者
     */
    @ColumnInsertOrUpdate(dateType = DateType.USER_ID)
    private Long updateBy;

    /**
     * 创建者
     */
    @ColumnInsert(dateType = DateType.USER_ID)
    private Long createBy;

    /**
     * 更新时间
     */
    @ColumnInsertOrUpdate(dateType = DateType.DATE)
    private Date updateTime;

    /**
     * 创建时间
     */
    @ColumnInsert(dateType = DateType.DATE)
    private Date createTime;

}
