package org.dromara.workflow.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.workflow.domain.WfFormManage;
import org.dromara.workflow.domain.query.WfFormManageQuery;
import org.dromara.workflow.domain.vo.WfFormManageVo;

import java.util.List;

/**
 * 表单管理Mapper接口
 *
 * @author may
 * @date 2024-03-29
 */
public interface WfFormManageMapper extends BaseMapperPlus<WfFormManage, WfFormManageVo> {

    /**
     * 查询表单管理列表
     *
     * @param query 查询对象
     * @return {@link WfFormManageVo}
     */
    List<WfFormManageVo> queryList(WfFormManageQuery query);
}
