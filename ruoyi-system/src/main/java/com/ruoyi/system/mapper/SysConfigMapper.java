package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysConfig;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author Lion Li
 */
public interface SysConfigMapper extends BaseMapperPlus<SysConfigMapper, SysConfig, SysConfig> {

    /**
     * 查询参数配置列表
     *
     * @param config
     * @return {@link SysConfig}
     */
    List<SysConfig> queryList(SysConfig config);
}
