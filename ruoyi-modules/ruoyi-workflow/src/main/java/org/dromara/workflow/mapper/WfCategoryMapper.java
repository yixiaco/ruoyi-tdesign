package org.dromara.workflow.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.workflow.domain.WfCategory;
import org.dromara.workflow.domain.query.WfCategoryQuery;
import org.dromara.workflow.domain.vo.WfCategoryVo;

import java.util.List;

/**
 * 流程分类Mapper接口
 *
 * @author may
 * @date 2023-06-27
 */
public interface WfCategoryMapper extends BaseMapperPlus<WfCategory, WfCategoryVo> {

    /**
     * 查询流程分类列表
     *
     * @param query 查询对象
     * @return {@link WfCategoryVo}
     */
    List<WfCategoryVo> queryList(WfCategoryQuery query);
}
