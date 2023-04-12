package org.dromara.system.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysNotice;
import org.dromara.system.domain.query.SysNoticeQuery;
import org.dromara.system.domain.vo.SysNoticeVo;

import java.util.List;

/**
 * 通知公告表 数据层
 *
 * @author Lion Li
 */
public interface SysNoticeMapper extends BaseMapperPlus<SysNotice, SysNoticeVo> {

    /**
     * 查询通知公告列表
     *
     * @param notice
     * @return {@link SysNotice}
     */
    List<SysNoticeVo> queryList(SysNoticeQuery notice);
}
