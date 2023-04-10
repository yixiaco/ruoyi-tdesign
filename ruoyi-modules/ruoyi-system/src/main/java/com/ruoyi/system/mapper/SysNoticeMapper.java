package com.ruoyi.system.mapper;

import com.ruoyi.common.mybatis.core.mapper.BaseMapperPlus;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.dto.SysNoticeQuery;
import com.ruoyi.system.domain.vo.SysNoticeVo;

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
