package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysDictType;
import org.dromara.system.domain.query.SysDictTypeQuery;
import org.dromara.system.domain.vo.SysDictTypeVo;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author Lion Li
 */
public interface SysDictTypeMapper extends BaseMapperPlus<SysDictType, SysDictTypeVo> {

    /**
     * 查询字典类型列表
     *
     * @param dictType
     * @return {@link SysDictType}
     */
    List<SysDictTypeVo> queryList(SysDictTypeQuery dictType);
}
