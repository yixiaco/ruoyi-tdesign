package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysClient;
import org.dromara.system.domain.query.SysClientQuery;
import org.dromara.system.domain.vo.SysClientVo;

import java.util.List;

/**
 * 系统授权Mapper接口
 *
 * @author Michelle.Chung
 * @date 2023-05-15
 */
public interface SysClientMapper extends BaseMapperPlus<SysClient, SysClientVo> {

    /**
     * 查询系统授权列表
     *
     * @param query 查询对象
     * @return {@link SysClientVo}
     */
    List<SysClientVo> queryList(SysClientQuery query);
}
