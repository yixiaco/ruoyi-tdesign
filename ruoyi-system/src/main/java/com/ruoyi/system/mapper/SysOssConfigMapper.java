package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysOssConfig;
import com.ruoyi.system.domain.bo.SysOssConfigBo;
import com.ruoyi.system.domain.vo.SysOssConfigVo;

import java.util.List;

/**
 * 对象存储配置Mapper接口
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
public interface SysOssConfigMapper extends BaseMapperPlus<SysOssConfigMapper, SysOssConfig, SysOssConfigVo> {

    /**
     * 查询对象存储配置列表
     *
     * @param bo bo对象
     * @return {@link SysOssConfigVo}
     */
    List<SysOssConfigVo> queryList(SysOssConfigBo bo);
}
