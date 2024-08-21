package org.dromara.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.SysSensitiveWordBo;
import org.dromara.system.domain.bo.SysSensitiveWordTestBo;
import org.dromara.system.domain.query.SysSensitiveWordQuery;
import org.dromara.system.domain.template.SysSensitiveWordImportTemplate;
import org.dromara.system.domain.vo.SysSensitiveWordTestVo;
import org.dromara.system.domain.vo.SysSensitiveWordVo;
import org.dromara.system.listener.SysSensitiveWordImportListener;
import org.dromara.system.service.ISysSensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 敏感词
 *
 * @author hexm
 * @date 2024-08-16
 */
@Validated
@RestController
@RequestMapping("/system/sensitiveWord")
public class SysSensitiveWordController extends BaseController {

    @Autowired
    private ISysSensitiveWordService sysSensitiveWordService;

    /**
     * 查询敏感词列表
     */
    @SaCheckPermission("system:sensitiveWord:list")
    @GetMapping("/list")
    public TableDataInfo<SysSensitiveWordVo> list(SysSensitiveWordQuery query) {
        return sysSensitiveWordService.queryPageList(query);
    }

    /**
     * 导出敏感词列表
     */
    @SaCheckPermission("system:sensitiveWord:export")
    @Log(title = "敏感词", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysSensitiveWordQuery query, HttpServletResponse response) {
        List<SysSensitiveWordVo> list = sysSensitiveWordService.queryList(query);
        ExcelUtil.exportExcel(list, "敏感词", SysSensitiveWordVo.class, response);
    }

    /**
     * 导入数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    @Log(title = "敏感词", businessType = BusinessType.IMPORT)
    @SaCheckPermission("system:sensitiveWord:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        SysSensitiveWordImportListener listener = new SysSensitiveWordImportListener(sysSensitiveWordService, updateSupport);
        ExcelResult<SysSensitiveWordImportTemplate> result = ExcelUtil.importExcel(file.getInputStream(), SysSensitiveWordImportTemplate.class, listener);
        return R.msg(result.getAnalysis());
    }

    /**
     * 获取导入模板
     */
    @SaCheckPermission("system:sensitiveWord:import")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "敏感词", SysSensitiveWordImportTemplate.class, response);
    }

    /**
     * 获取敏感词详细信息
     *
     * @param wordId 主键
     */
    @SaCheckPermission(value = {"system:sensitiveWord:query", "system:sensitiveWord:edit"}, mode = SaMode.OR)
    @GetMapping("/{wordId}")
    public R<SysSensitiveWordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long wordId) {
        return R.ok(sysSensitiveWordService.queryById(wordId));
    }

    /**
     * 新增敏感词
     */
    @SaCheckPermission("system:sensitiveWord:add")
    @Log(title = "敏感词", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysSensitiveWordBo bo) {
        return toAjax(sysSensitiveWordService.insertByBo(bo));
    }

    /**
     * 修改敏感词
     */
    @SaCheckPermission("system:sensitiveWord:edit")
    @Log(title = "敏感词", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysSensitiveWordBo bo) {
        return toAjax(sysSensitiveWordService.updateByBo(bo));
    }

    /**
     * 删除敏感词
     *
     * @param wordIds 主键串
     */
    @SaCheckPermission("system:sensitiveWord:remove")
    @Log(title = "敏感词", businessType = BusinessType.DELETE)
    @DeleteMapping("/{wordIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] wordIds) {
        return toAjax(sysSensitiveWordService.deleteWithValidByIds(List.of(wordIds)));
    }

    /**
     * 刷新敏感词缓存
     */
    @SaCheckPermission("system:sensitiveWord:remove")
    @Log(title = "敏感词", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        sysSensitiveWordService.refreshCache();
        return R.ok();
    }

    /**
     * 测试敏感词
     */
    @SaCheckPermission("system:sensitiveWord:list")
    @GetMapping("/test")
    public R<SysSensitiveWordTestVo> sensitiveWordTest(@Validated SysSensitiveWordTestBo bo) {
        SysSensitiveWordTestVo testVo = new SysSensitiveWordTestVo();
        testVo.setContainsSensitiveWord(sysSensitiveWordService.containsSensitiveWord(bo.getText(), bo.getCategory()));
        testVo.setSensitiveWordReplace(sysSensitiveWordService.sensitiveWordReplace(bo.getText(), bo.getCategory()));
        testVo.setSensitiveWords(sysSensitiveWordService.getFoundAllSensitive(bo.getText(), bo.getCategory()));
        return R.ok(testVo);
    }
}
