package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 字典数据查询对象 sys_dict_data
 *
 * @author hexm
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataQuery extends BaseEntity {

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典类型
     */
    private String dictType;

}
