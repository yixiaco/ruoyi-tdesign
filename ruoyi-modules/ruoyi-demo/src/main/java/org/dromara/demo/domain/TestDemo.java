package org.dromara.demo.domain;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.annotation.ColumnInsert;
import org.dromara.common.mybatis.annotation.ColumnInsertOrUpdate;
import org.dromara.common.mybatis.enums.DateType;
import org.dromara.common.tenant.core.TenantEntity;
import org.springframework.data.annotation.Version;

import java.io.Serial;
import java.util.Date;

/**
 * 测试单表对象 test_demo
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("test_demo")
public class TestDemo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 排序号
     */
    private Integer orderNum;

    /**
     * key键
     */
    private String testKey;

    /**
     * 值
     */
    private String value;

    /**
     * 版本
     */
    @Version
    @ColumnInsert(dateType = DateType.ZERO)
    private Long version;

    /**
     * 删除标志
     */
    @ColumnInsert(dateType = DateType.LOGIC_NOT_DELETE)
    private Long delFlag;


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

}
