package org.dromara.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.query.SysMessageLogQuery;
import org.dromara.system.domain.vo.SysMessageLogVo;
import org.dromara.system.service.ISysMessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息发送记录
 *
 * @author hexm
 * @date 2023-06-29
 */
@Validated
@RestController
@RequestMapping("/system/messageLog")
public class SysMessageLogController extends BaseController {

    @Autowired
    private ISysMessageLogService sysMessageLogService;

    /**
     * 查询消息发送记录列表
     */
    @SaCheckPermission("system:messageLog:list")
    @GetMapping("/list")
    public TableDataInfo<SysMessageLogVo> list(SysMessageLogQuery query) {
        return sysMessageLogService.queryPageList(query);
    }

    /**
     * 导出消息发送记录列表
     */
    @SaCheckPermission("system:messageLog:export")
    @Log(title = "消息发送记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMessageLogQuery query, HttpServletResponse response) {
        List<SysMessageLogVo> list = sysMessageLogService.queryList(query);
        ExcelUtil.exportExcel(list, "消息发送记录", SysMessageLogVo.class, response);
    }

    /**
     * 获取消息发送记录详细信息
     *
     * @param messageLogId 主键
     */
    @SaCheckPermission(value = {"system:messageLog:query", "system:messageLog:edit"}, mode = SaMode.OR)
    @GetMapping("/{messageLogId}")
    public R<SysMessageLogVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long messageLogId) {
        return R.ok(sysMessageLogService.queryById(messageLogId));
    }

    /**
     * 删除消息发送记录
     *
     * @param messageLogIds 主键串
     */
    @SaCheckPermission("system:messageLog:remove")
    @Log(title = "消息发送记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageLogIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] messageLogIds) {
        return toAjax(sysMessageLogService.deleteWithValidByIds(List.of(messageLogIds)));
    }

    /**
     * 清空消息发送记录
     */
    @SaCheckPermission("system:messageLog:remove")
    @Log(title = "消息发送记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/clear")
    public R<Void> clear() {
        return toAjax(sysMessageLogService.clear());
    }
}
