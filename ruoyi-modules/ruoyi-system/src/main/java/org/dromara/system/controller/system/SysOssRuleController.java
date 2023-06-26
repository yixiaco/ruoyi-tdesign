package org.dromara.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
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
import org.dromara.system.domain.bo.SysOssRuleBo;
import org.dromara.system.domain.query.SysOssRuleQuery;
import org.dromara.system.domain.vo.SysOssRuleVo;
import org.dromara.system.service.ISysOssRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OSS处理规则
 *
 * @author hexm
 * @date 2023-05-05
 */
@Validated
@RestController
@RequestMapping("/system/ossRule")
public class SysOssRuleController extends BaseController {

    @Autowired
    private ISysOssRuleService sysOssRuleService;

    /**
     * 查询OSS处理规则列表
     */
    @SaCheckPermission("system:ossRule:list")
    @GetMapping("/list")
    public TableDataInfo<SysOssRuleVo> list(SysOssRuleQuery query) {
        return sysOssRuleService.queryPageList(query);
    }

    /**
     * 导出OSS处理规则列表
     */
    @SaCheckPermission("system:ossRule:export")
    @Log(title = "OSS处理规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOssRuleQuery query, HttpServletResponse response) {
        List<SysOssRuleVo> list = sysOssRuleService.queryList(query);
        ExcelUtil.exportExcel(list, "OSS处理规则", SysOssRuleVo.class, response);
    }

    /**
     * 获取OSS处理规则详细信息
     *
     * @param ossRuleId 主键
     */
    @SaCheckPermission(value = {"system:ossRule:query", "system:ossRule:edit"}, mode = SaMode.OR)
    @GetMapping("/{ossRuleId}")
    public R<SysOssRuleVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long ossRuleId) {
        return R.ok(sysOssRuleService.queryById(ossRuleId));
    }

    /**
     * 新增OSS处理规则
     */
    @SaCheckPermission("system:ossRule:add")
    @Log(title = "OSS处理规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOssRuleBo bo) {
        return toAjax(sysOssRuleService.insertByBo(bo));
    }

    /**
     * 修改OSS处理规则
     */
    @SaCheckPermission("system:ossRule:edit")
    @Log(title = "OSS处理规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOssRuleBo bo) {
        return toAjax(sysOssRuleService.updateByBo(bo));
    }

    /**
     * 删除OSS处理规则
     *
     * @param ossRuleIds 主键串
     */
    @SaCheckPermission("system:ossRule:remove")
    @Log(title = "OSS处理规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossRuleIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ossRuleIds) {
        return toAjax(sysOssRuleService.deleteWithValidByIds(List.of(ossRuleIds)));
    }

    /**
     * 刷新OSS规则缓存
     */
    @SaCheckPermission(value = {"system:ossRule:remove", "system:ossRule:edit", "system:ossRule:add"}, mode = SaMode.OR)
    @Log(title = "OSS规则", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        sysOssRuleService.removeCache();
        return R.ok();
    }

    /**
     * 规则覆盖字段值修改
     */
    @SaCheckPermission("system:ossRule:edit")
    @Log(title = "OSS规则", businessType = BusinessType.UPDATE)
    @PutMapping("/changeOverwrite")
    public R<Void> changeStatus(@NotNull(message = "规则id不能为空") Long ossRuleId,
                                @NotBlank(message = "是否覆盖字段值不能为空") String isOverwrite) {
        sysOssRuleService.updateOverwrite(ossRuleId, isOverwrite);
        return R.ok();
    }
}
