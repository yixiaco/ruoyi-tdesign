package com.ruoyi.system.mapper;

import com.ruoyi.common.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysNotice;

import java.util.List;

/**
 * 通知公告表 数据层
 *
 * @author Lion Li
 */
public interface SysNoticeMapper extends BaseMapperPlus<SysNoticeMapper, SysNotice, SysNotice> {

    /**
     * 查询通知公告列表
     *
     * @param notice
     * @return {@link SysNotice}
     */
    List<SysNotice> queryList(SysNotice notice);
}
