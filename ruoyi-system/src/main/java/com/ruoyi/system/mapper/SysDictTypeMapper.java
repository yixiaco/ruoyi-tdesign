package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author Lion Li
 */
public interface SysDictTypeMapper extends BaseMapperPlus<SysDictTypeMapper, SysDictType, SysDictType> {

    /**
     * 查询字典类型列表
     *
     * @param dictType
     * @return {@link SysDictType}
     */
    List<SysDictType> queryList(SysDictType dictType);
}
