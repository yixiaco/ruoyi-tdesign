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
import org.dromara.system.domain.bo.SysAppBo;
import org.dromara.system.domain.query.SysAppQuery;
import org.dromara.system.domain.vo.SysAppVo;
import org.dromara.system.service.ISysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用管理
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Validated
@RestController
@RequestMapping("/system/app")
public class SysAppController extends BaseController {

    @Autowired
    private ISysAppService sysAppService;

    /**
     * 查询应用管理列表
     */
    @SaCheckPermission("system:app:list")
    @GetMapping("/list")
    public TableDataInfo<SysAppVo> list(SysAppQuery query) {
        return sysAppService.queryPageList(query);
    }

    /**
     * 导出应用管理列表
     */
    @SaCheckPermission("system:app:export")
    @Log(title = "应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysAppQuery query, HttpServletResponse response) {
        List<SysAppVo> list = sysAppService.queryList(query);
        ExcelUtil.exportExcel(list, "应用管理", SysAppVo.class, response);
    }

    /**
     * 获取应用管理详细信息
     *
     * @param appid 主键
     */
    @SaCheckPermission(value = {"system:app:query", "system:app:edit"}, mode = SaMode.OR)
    @GetMapping("/{appid}")
    public R<SysAppVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long appid) {
        return R.ok(sysAppService.queryById(appid));
    }

    /**
     * 新增应用管理
     */
    @SaCheckPermission("system:app:add")
    @Log(title = "应用管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysAppBo bo) {
        return toAjax(sysAppService.insertByBo(bo));
    }

    /**
     * 修改应用管理
     */
    @SaCheckPermission("system:app:edit")
    @Log(title = "应用管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysAppBo bo) {
        return toAjax(sysAppService.updateByBo(bo));
    }

    /**
     * 删除应用管理
     *
     * @param appids 主键串
     */
    @SaCheckPermission("system:app:remove")
    @Log(title = "应用管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] appids) {
        return toAjax(sysAppService.deleteWithValidByIds(List.of(appids)));
    }
}
