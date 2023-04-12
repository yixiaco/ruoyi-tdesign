package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysOssConfig;
import org.dromara.system.domain.query.SysOssConfigQuery;
import org.dromara.system.domain.vo.SysOssConfigVo;

import java.util.List;

/**
 * 对象存储配置Mapper接口
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
public interface SysOssConfigMapper extends BaseMapperPlus<SysOssConfig, SysOssConfigVo> {

    /**
     * 查询对象存储配置列表
     *
     * @param query 查询对象
     * @return {@link SysOssConfigVo}
     */
    List<SysOssConfigVo> queryList(SysOssConfigQuery query);
}
