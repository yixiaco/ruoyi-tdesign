package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysMessageKey;
import org.dromara.system.domain.query.SysMessageKeyQuery;
import org.dromara.system.domain.vo.SysMessageKeyVo;

import java.util.List;

/**
 * 消息常量Mapper接口
 *
 * @author hexm
 * @date 2023-06-28
 */
public interface SysMessageKeyMapper extends BaseMapperPlus<SysMessageKey, SysMessageKeyVo> {

    /**
     * 查询消息常量列表
     *
     * @param query 查询对象
     * @return {@link SysMessageKeyVo}
     */
    List<SysMessageKeyVo> queryList(SysMessageKeyQuery query);
}
