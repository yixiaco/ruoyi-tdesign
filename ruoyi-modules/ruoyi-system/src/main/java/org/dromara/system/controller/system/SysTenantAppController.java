package org.dromara.system.controller.system;

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
import org.dromara.system.domain.bo.SysTenantAppBo;
import org.dromara.system.domain.query.SysTenantAppQuery;
import org.dromara.system.domain.vo.SysTenantAppVo;
import org.dromara.system.service.ISysTenantAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户应用管理
 *
 * @author yixiacoco
 * @date 2023-05-17
 */
@Validated
@RestController
@RequestMapping("/system/tenantApp")
public class SysTenantAppController extends BaseController {

    @Autowired
    private ISysTenantAppService sysTenantAppService;

    /**
     * 查询租户应用管理列表
     */
    @SaCheckPermission("system:tenantApp:list")
    @GetMapping("/list")
    public TableDataInfo<SysTenantAppVo> list(SysTenantAppQuery query) {
        return sysTenantAppService.queryPageList(query);
    }

    /**
     * 导出租户应用管理列表
     */
    @SaCheckPermission("system:tenantApp:export")
    @Log(title = "租户应用管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysTenantAppQuery query, HttpServletResponse response) {
        List<SysTenantAppVo> list = sysTenantAppService.queryList(query);
        ExcelUtil.exportExcel(list, "租户应用管理", SysTenantAppVo.class, response);
    }

    /**
     * 获取租户应用管理详细信息
     *
     * @param appid 主键
     */
    @SaCheckPermission(value = {"system:tenantApp:query", "system:tenantApp:edit"}, mode = SaMode.OR)
    @GetMapping("/{appid}")
    public R<SysTenantAppVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long appid) {
        return R.ok(sysTenantAppService.queryById(appid));
    }

    /**
     * 新增租户应用管理
     */
    @SaCheckPermission("system:tenantApp:add")
    @Log(title = "租户应用管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysTenantAppBo bo) {
        return toAjax(sysTenantAppService.insertByBo(bo));
    }

    /**
     * 修改租户应用管理
     */
    @SaCheckPermission("system:tenantApp:edit")
    @Log(title = "租户应用管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysTenantAppBo bo) {
        return toAjax(sysTenantAppService.updateByBo(bo));
    }

    /**
     * 删除租户应用管理
     *
     * @param appids 主键串
     */
    @SaCheckPermission("system:tenantApp:remove")
    @Log(title = "租户应用管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] appids) {
        return toAjax(sysTenantAppService.deleteWithValidByIds(List.of(appids)));
    }
}
