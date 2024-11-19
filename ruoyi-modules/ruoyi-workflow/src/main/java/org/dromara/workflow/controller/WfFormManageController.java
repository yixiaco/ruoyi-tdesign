package org.dromara.workflow.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.workflow.domain.bo.WfFormManageBo;
import org.dromara.workflow.domain.query.WfFormManageQuery;
import org.dromara.workflow.domain.vo.WfFormManageVo;
import org.dromara.workflow.service.IWfFormManageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表单管理
 *
 * @author may
 * @date 2024-03-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/formManage")
public class WfFormManageController extends BaseController {

    private final IWfFormManageService wfFormManageService;

    /**
     * 查询表单管理列表
     */
    @SaCheckPermission("workflow:formManage:list")
    @GetMapping("/list")
    public TableDataInfo<WfFormManageVo> list(WfFormManageQuery query) {
        return wfFormManageService.queryPageList(query);
    }

    /**
     * 查询表单管理列表
     */
    @SaCheckPermission("workflow:formManage:list")
    @GetMapping("/list/selectList")
    public R<List<WfFormManageVo>> selectList() {
        return R.ok(wfFormManageService.selectList());
    }

    /**
     * 导出表单管理列表
     */
    @SaCheckPermission("workflow:formManage:export")
    @Log(title = "表单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfFormManageQuery query, HttpServletResponse response) {
        List<WfFormManageVo> list = wfFormManageService.queryList(query);
        ExcelUtil.exportExcel(list, "表单管理", WfFormManageVo.class, response);
    }

    /**
     * 获取表单管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission(value = {"workflow:formManage:query", "workflow:formManage:edit"}, mode = SaMode.OR)
    @GetMapping("/{id}")
    public R<WfFormManageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wfFormManageService.queryById(id));
    }

    /**
     * 新增表单管理
     */
    @SaCheckPermission("workflow:formManage:add")
    @Log(title = "表单管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfFormManageBo bo) {
        return toAjax(wfFormManageService.insertByBo(bo));
    }

    /**
     * 修改表单管理
     */
    @SaCheckPermission("workflow:formManage:edit")
    @Log(title = "表单管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfFormManageBo bo) {
        return toAjax(wfFormManageService.updateByBo(bo));
    }

    /**
     * 删除表单管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:formManage:remove")
    @Log(title = "表单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wfFormManageService.deleteByIds(List.of(ids)));
    }
}