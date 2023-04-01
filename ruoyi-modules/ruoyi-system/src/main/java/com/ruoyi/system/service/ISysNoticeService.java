package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.mybatis.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.bo.SysNoticeBo;
import com.ruoyi.system.domain.dto.SysNoticeQuery;
import com.ruoyi.system.domain.vo.SysNoticeVo;

import java.util.List;

/**
 * 公告 服务层
 *
 * @author Lion Li
 */
public interface ISysNoticeService extends IService<SysNotice> {


    TableDataInfo<SysNoticeVo> selectPageNoticeList(SysNoticeQuery notice);

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNotice selectNoticeById(Long noticeId);

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    List<SysNoticeVo> selectNoticeList(SysNoticeQuery notice);

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    Boolean insertNotice(SysNoticeBo notice);

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    Boolean updateNotice(SysNoticeBo notice);

    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    int deleteNoticeById(Long noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    int deleteNoticeByIds(Long[] noticeIds);
}
