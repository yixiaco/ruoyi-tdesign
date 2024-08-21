package org.dromara.system.mapper;

import org.dromara.system.domain.SysSensitiveWord;
import org.dromara.system.domain.bo.SysSensitiveWordBo;
import org.dromara.system.domain.query.SysSensitiveWordQuery;
import org.dromara.system.domain.vo.SysSensitiveWordVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 敏感词Mapper接口
 *
 * @author hexm
 * @date 2024-08-16
 */
public interface SysSensitiveWordMapper extends BaseMapperPlus<SysSensitiveWord, SysSensitiveWordVo> {

    /**
     * 查询敏感词列表
     *
     * @param query 查询对象
     * @return {@link SysSensitiveWordVo}
     */
    List<SysSensitiveWordVo> queryList(SysSensitiveWordQuery query);
}
