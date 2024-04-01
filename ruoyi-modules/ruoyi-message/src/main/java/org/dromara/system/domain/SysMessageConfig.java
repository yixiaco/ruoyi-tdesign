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
 * 消息配置对象 sys_message_config
 *
 * @author hexm
 * @date 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("sys_message_config")
public class SysMessageConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息设置id
     */
    @Id
    private Long messageConfigId;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 支持平台标识
     */
    private String supplierType;

    /**
     * 配置json
     */
    private String configJson;

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
