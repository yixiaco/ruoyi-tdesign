package org.dromara.system.controller.system;

import java.util.List;

import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import org.dromara.common.core.enums.UserType;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.system.domain.bo.SysOssCategoryBo;
import org.dromara.system.domain.query.SysOssCategoryQuery;
import org.dromara.system.domain.vo.SysOssCategoryVo;
import org.dromara.system.service.ISysOssCategoryService;

/**
 * OSS分类
 *
 * @author hexm
 * @date 2023-08-14
 */
@Validated
@RestController
@RequestMapping("/system/ossCategory")
public class SysOssCategoryController extends BaseController {

    @Autowired
    private ISysOssCategoryService sysOssCategoryService;

    /**
     * 查询OSS分类列表
     */
    @SaCheckPermission("system:ossCategory:list")
    @GetMapping("/list")
    public R<List<SysOssCategoryVo>> list(SysOssCategoryQuery query) {
        query.setUserType(UserType.SYS_USER.getUserType());
        query.setCreateBy(LoginHelper.getUserId());
        List<SysOssCategoryVo> list = sysOssCategoryService.queryList(query);
        return R.ok(list);
    }

    /**
     * 获取OSS分类详细信息
     *
     * @param ossCategoryId 主键
     */
    @SaCheckPermission(value = {"system:ossCategory:query", "system:ossCategory:edit"}, mode = SaMode.OR)
    @GetMapping("/{ossCategoryId}")
    public R<SysOssCategoryVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long ossCategoryId) {
        return R.ok(sysOssCategoryService.queryById(ossCategoryId));
    }

    /**
     * 新增OSS分类
     */
    @SaCheckPermission("system:ossCategory:add")
    @Log(title = "OSS分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOssCategoryBo bo) {
        bo.setCreateBy(LoginHelper.getUserId());
        bo.setUserType(UserType.SYS_USER.getUserType());
        return toAjax(sysOssCategoryService.insertByBo(bo));
    }

    /**
     * 修改OSS分类
     */
    @SaCheckPermission("system:ossCategory:edit")
    @Log(title = "OSS分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOssCategoryBo bo) {
        return toAjax(sysOssCategoryService.updateByBo(bo));
    }

    /**
     * 删除OSS分类
     *
     * @param ossCategoryIds 主键串
     */
    @SaCheckPermission("system:ossCategory:remove")
    @Log(title = "OSS分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossCategoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ossCategoryIds) {
        return toAjax(sysOssCategoryService.deleteWithValidByIds(List.of(ossCategoryIds)));
    }
}
