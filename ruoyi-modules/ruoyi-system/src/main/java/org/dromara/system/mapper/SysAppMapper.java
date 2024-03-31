package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.MyBaseMapperVo;
import org.dromara.system.domain.SysApp;
import org.dromara.system.domain.query.SysAppQuery;
import org.dromara.system.domain.vo.SysAppVo;

import java.util.List;

/**
 * 应用管理Mapper接口
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
public interface SysAppMapper extends MyBaseMapperVo<SysApp, SysAppVo> {

    /**
     * 查询应用管理列表
     *
     * @param query 查询对象
     * @return {@link SysAppVo}
     */
    List<SysAppVo> queryList(SysAppQuery query);
}
