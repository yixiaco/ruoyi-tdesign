package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.domain.bo.SysConfigBo;
import com.ruoyi.system.domain.vo.SysConfigVo;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author Lion Li
 */
public interface SysConfigMapper extends BaseMapperPlus<SysConfig, SysConfigVo> {

    /**
     * 查询参数配置列表
     *
     * @param config
     * @return {@link SysConfig}
     */
    List<SysConfigVo> queryList(SysConfigBo config);
}
