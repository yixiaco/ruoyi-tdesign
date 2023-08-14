package org.dromara.system.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.enums.UserType;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.system.domain.SysOss;

/**
 * OSS对象存储业务对象 sys_oss
 *
 * @author yixiacoco
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysOss.class, reverseConvertGenerate = false)
public class SysOssBo extends BaseEntity {

    /**
     * 对象存储主键
     */
    private Long ossId;

    /**
     * 分类id
     */
    private Long ossCategoryId = 0L;

    /**
     * 用户类型
     */
    private UserType userType;

    /**
     * 是否锁定状态
     */
    private Integer isLock = 0;

    /**
     * 是否显示在列表
     */
    private Integer isList = 1;

    /**
     * 上传人
     */
    private Long createBy;

}
