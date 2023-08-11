package org.dromara.generator.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.util.List;

/**
 * 代码生成业务查询对象 gen_table
 *
 * @author yixiacoco
 * @date 2023-08-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GenTableQuery extends BaseEntity {

    /**
     * 数据源名称
     */
    private String dataName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 已生成的表名称
     */
    private List<String> genTableNames;

}
