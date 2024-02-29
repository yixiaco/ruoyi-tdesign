package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysOssCategory;
import org.dromara.system.domain.query.SysOssCategoryQuery;
import org.dromara.system.domain.vo.SysOssCategoryVo;

import java.util.List;

/**
 * OSS分类Mapper接口
 *
 * @author hexm
 * @date 2023-08-14
 */
public interface SysOssCategoryMapper extends BaseMapperPlus<SysOssCategory, SysOssCategoryVo> {

    /**
     * 查询OSS分类列表
     *
     * @param query 查询对象
     * @return {@link SysOssCategoryVo}
     */
    List<SysOssCategoryVo> queryList(SysOssCategoryQuery query);

    /**
     * 查询OSS分类
     *
     * @param query 查询对象
     * @return
     */
    SysOssCategoryVo queryVoById(SysOssCategoryQuery query);
}
