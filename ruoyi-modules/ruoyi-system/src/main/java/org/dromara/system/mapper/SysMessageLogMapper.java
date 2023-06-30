package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysMessageLog;
import org.dromara.system.domain.query.SysMessageLogQuery;
import org.dromara.system.domain.vo.SysMessageLogVo;

import java.util.List;

/**
 * 消息发送记录Mapper接口
 *
 * @author hexm
 * @date 2023-06-29
 */
public interface SysMessageLogMapper extends BaseMapperPlus<SysMessageLog, SysMessageLogVo> {

    /**
     * 查询消息发送记录列表
     *
     * @param query 查询对象
     * @return {@link SysMessageLogVo}
     */
    List<SysMessageLogVo> queryList(SysMessageLogQuery query);
}
