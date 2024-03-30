package org.dromara.system.mapper;

import com.mybatisflex.core.BaseMapper;
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
public interface SysMessageLogMapper extends BaseMapper<SysMessageLog> {

    /**
     * 查询消息发送记录列表
     *
     * @param query 查询对象
     * @return {@link SysMessageLogVo}
     */
    List<SysMessageLogVo> queryList(SysMessageLogQuery query);
}
