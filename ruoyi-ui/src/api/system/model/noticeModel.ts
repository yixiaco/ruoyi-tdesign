import { BaseEntity } from '@/api/model/resultModel';

/**
 * 通知公告
 */
export interface SysNotice extends BaseEntity {
  /**
   * 公告ID
   */
  noticeId?: number;

  /**
   * 公告标题
   */
  noticeTitle?: string;

  /**
   * 公告类型（1通知 2公告）
   */
  noticeType?: string;

  /**
   * 公告内容
   */
  noticeContent?: string;

  /**
   * 公告状态（0正常 1关闭）
   */
  status?: string;

  /**
   * 备注
   */
  remark?: string;

  /**
   * 创建者
   */
  createBy?: string;

  /**
   * 创建时间
   */
  createTime?: string;

  /**
   * 更新者
   */
  updateBy?: string;

  /**
   * 更新时间
   */
  updateTime?: string;
}
