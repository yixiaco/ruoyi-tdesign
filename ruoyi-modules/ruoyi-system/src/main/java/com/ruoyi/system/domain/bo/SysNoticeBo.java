package com.ruoyi.system.domain.bo;

import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.web.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告业务对象 sys_notice
 *
 * @author ruoyi
 * @date 2023-04-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeBo extends BaseEntity {

    /**
     * 公告ID
     */
    @NotNull(message = "公告ID不能为空", groups = {EditGroup.class})
    private Long noticeId;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @NotBlank(message = "公告类型（1通知 2公告）不能为空", groups = {AddGroup.class, EditGroup.class})
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

}
