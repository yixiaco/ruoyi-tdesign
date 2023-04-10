package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.domain.bo.SysOperLogBo;
import com.ruoyi.system.domain.vo.SysOperLogVo;

import java.util.List;

/**
 * 操作日志 数据层
 *
 * @author Lion Li
 */
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLog, SysOperLogVo> {

    /**
     * 查询操作日志记录列表
     *
     * @param operLog
     * @return {@link SysOperLog}
     */
    List<SysOperLogVo> queryList(SysOperLogBo operLog);
}
