package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysMessageTemplate;
import org.dromara.system.domain.query.SysMessageTemplateQuery;
import org.dromara.system.domain.vo.SysMessageTemplateVo;

import java.util.List;

/**
 * 消息模板Mapper接口
 *
 * @author hexm
 * @date 2023-06-28
 */
public interface SysMessageTemplateMapper extends BaseMapperPlus<SysMessageTemplate, SysMessageTemplateVo> {

    /**
     * 查询消息模板列表
     *
     * @param query 查询对象
     * @return {@link SysMessageTemplateVo}
     */
    List<SysMessageTemplateVo> queryList(SysMessageTemplateQuery query);
}
