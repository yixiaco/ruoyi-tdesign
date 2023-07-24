package org.dromara.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.SysMessageKeyBo;
import org.dromara.system.domain.query.SysMessageKeyQuery;
import org.dromara.system.domain.vo.SysMessageKeyVo;
import org.dromara.system.service.ISysMessageKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息常量
 *
 * @author hexm
 * @date 2023-06-28
 */
@Validated
@RestController
@RequestMapping("/system/messageKey")
public class SysMessageKeyController extends BaseController {

    @Autowired
    private ISysMessageKeyService sysMessageKeyService;

    /**
     * 查询消息常量列表
     */
    @SaCheckPermission("system:messageKey:list")
    @GetMapping("/list")
    public TableDataInfo<SysMessageKeyVo> list(SysMessageKeyQuery query) {
        return sysMessageKeyService.queryPageList(query);
    }

    /**
     * 导出消息常量列表
     */
    @SaCheckPermission("system:messageKey:export")
    @Log(title = "消息常量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMessageKeyQuery query, HttpServletResponse response) {
        List<SysMessageKeyVo> list = sysMessageKeyService.queryList(query);
        ExcelUtil.exportExcel(list, "消息常量", SysMessageKeyVo.class, response);
    }

    /**
     * 获取消息常量详细信息
     *
     * @param messageKeyId 主键
     */
    @SaCheckPermission(value = {"system:messageKey:query", "system:messageKey:edit"}, mode = SaMode.OR)
    @GetMapping("/{messageKeyId}")
    public R<SysMessageKeyVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long messageKeyId) {
        return R.ok(sysMessageKeyService.queryById(messageKeyId));
    }

    /**
     * 新增消息常量
     */
    @SaCheckPermission("system:messageKey:add")
    @Log(title = "消息常量", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysMessageKeyBo bo) {
        return toAjax(sysMessageKeyService.insertByBo(bo));
    }

    /**
     * 修改消息常量
     */
    @SaCheckPermission("system:messageKey:edit")
    @Log(title = "消息常量", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysMessageKeyBo bo) {
        return toAjax(sysMessageKeyService.updateByBo(bo));
    }

    /**
     * 删除消息常量
     *
     * @param messageKeyIds 主键串
     */
    @SaCheckPermission("system:messageKey:remove")
    @Log(title = "消息常量", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageKeyIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] messageKeyIds) {
        return toAjax(sysMessageKeyService.deleteWithValidByIds(List.of(messageKeyIds)));
    }
}
