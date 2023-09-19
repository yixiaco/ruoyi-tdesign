package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 字典类型查询对象 sys_dict_type
 *
 * @author hexm
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictTypeQuery extends BaseEntity {

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

}
