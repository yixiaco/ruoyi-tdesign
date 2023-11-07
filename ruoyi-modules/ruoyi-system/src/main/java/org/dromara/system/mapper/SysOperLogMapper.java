package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysOperLog;
import org.dromara.system.domain.query.SysOperLogQuery;
import org.dromara.system.domain.vo.SysOperLogVo;

import java.util.List;

/**
 * 操作日志 数据层
 *
 * @author hexm
 */
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLog, SysOperLogVo> {

    /**
     * 查询操作日志记录列表
     *
     * @param query 查询对象
     * @return {@link SysOperLogVo}
     */
    List<SysOperLogVo> queryList(SysOperLogQuery query);
}
