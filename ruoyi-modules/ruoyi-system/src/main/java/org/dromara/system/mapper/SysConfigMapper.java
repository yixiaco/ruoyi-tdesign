package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysConfig;
import org.dromara.system.domain.bo.SysConfigBo;
import org.dromara.system.domain.vo.SysConfigVo;

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
