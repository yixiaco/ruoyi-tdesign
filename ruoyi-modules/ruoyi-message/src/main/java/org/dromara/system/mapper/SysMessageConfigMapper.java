package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.query.SysMessageConfigQuery;
import org.dromara.system.domain.vo.SysMessageConfigVo;

import java.util.List;

/**
 * 消息配置Mapper接口
 *
 * @author hexm
 * @date 2023-06-27
 */
public interface SysMessageConfigMapper extends BaseMapperPlus<SysMessageConfig, SysMessageConfigVo> {

    /**
     * 查询消息配置列表
     *
     * @param query 查询对象
     * @return {@link SysMessageConfigVo}
     */
    List<SysMessageConfigVo> queryList(SysMessageConfigQuery query);
}
