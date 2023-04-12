package org.dromara.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 通知公告查询对象 sys_notice
 *
 * @author ruoyi
 * @date 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeQuery extends BaseEntity {

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private String noticeType;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    /**
     * 创建人
     */
    private String createByName;

}
