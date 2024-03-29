package org.dromara.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.enums.NormalDisableEnum;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.SysMessageConfig;
import org.dromara.system.domain.SysMessageKey;
import org.dromara.system.domain.bo.SysMessageTemplateBo;
import org.dromara.system.domain.bo.SysMessageTemplateTestBo;
import org.dromara.system.domain.query.SysMessageTemplateQuery;
import org.dromara.system.domain.vo.SysMessageTemplateVo;
import org.dromara.system.service.ISysMessageConfigService;
import org.dromara.system.service.ISysMessageKeyService;
import org.dromara.system.service.ISysMessageSendService;
import org.dromara.system.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息模板
 *
 * @author hexm
 * @date 2023-06-28
 */
@Validated
@RestController
@RequestMapping("/system/messageTemplate")
public class SysMessageTemplateController extends BaseController {

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private ISysMessageConfigService messageConfigService;
    @Autowired
    private ISysMessageKeyService messageKeyService;
    @Autowired
    private ISysMessageSendService messageSendService;

    /**
     * 查询消息模板列表
     */
    @SaCheckPermission("system:messageTemplate:list")
    @GetMapping("/list")
    public TableDataInfo<SysMessageTemplateVo> list(SysMessageTemplateQuery query) {
        return sysMessageTemplateService.queryPageList(query);
    }

    /**
     * 导出消息模板列表
     */
    @SaCheckPermission("system:messageTemplate:export")
    @Log(title = "消息模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMessageTemplateQuery query, HttpServletResponse response) {
        List<SysMessageTemplateVo> list = sysMessageTemplateService.queryList(query);
        ExcelUtil.exportExcel(list, "消息模板", SysMessageTemplateVo.class, response);
    }

    /**
     * 获取消息模板详细信息
     *
     * @param messageTemplateId 主键
     */
    @SaCheckPermission(value = {"system:messageTemplate:query", "system:messageTemplate:edit", "system:messageTemplate:test"}, mode = SaMode.OR)
    @GetMapping("/{messageTemplateId}")
    public R<SysMessageTemplateVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long messageTemplateId) {
        return R.ok(sysMessageTemplateService.queryById(messageTemplateId));
    }

    /**
     * 新增消息模板
     */
    @SaCheckPermission("system:messageTemplate:add")
    @Log(title = "消息模板", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysMessageTemplateBo bo) {
        return toAjax(sysMessageTemplateService.insertByBo(bo));
    }

    /**
     * 修改消息模板
     */
    @SaCheckPermission("system:messageTemplate:edit")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysMessageTemplateBo bo) {
        return toAjax(sysMessageTemplateService.updateByBo(bo));
    }

    /**
     * 删除消息模板
     *
     * @param messageTemplateIds 主键串
     */
    @SaCheckPermission("system:messageTemplate:remove")
    @Log(title = "消息模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{messageTemplateIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] messageTemplateIds) {
        return toAjax(sysMessageTemplateService.deleteWithValidByIds(List.of(messageTemplateIds)));
    }

    /**
     * 获取消息配置
     *
     * @return
     */
    @GetMapping("/messageConfigs")
    @SaCheckPermission(value = {"system:messageTemplate:query", "system:messageTemplate:edit"}, mode = SaMode.OR)
    public R<List<SysMessageConfig>> getMessageConfigs(@NotBlank(message = "消息类型不能为空") String messageType) {
        List<SysMessageConfig> list = messageConfigService.lambdaQuery()
            .eq(SysMessageConfig::getStatus, NormalDisableEnum.NORMAL.getCode())
            .eq(SysMessageConfig::getMessageType, messageType)
            .select(SysMessageConfig::getMessageConfigId,
                SysMessageConfig::getMessageType,
                SysMessageConfig::getTitle,
                SysMessageConfig::getSupplierType)
            .orderByDesc(SysMessageConfig::getCreateTime)
            .list();
        return R.ok(list);
    }

    /**
     * 获取消息常量
     *
     * @return
     */
    @GetMapping("/messageKeys")
    @SaCheckPermission(value = {"system:messageTemplate:query", "system:messageTemplate:edit"}, mode = SaMode.OR)
    public R<List<SysMessageKey>> getMessageKeys() {
        List<SysMessageKey> list = messageKeyService.lambdaQuery()
            .select(SysMessageKey::getName, SysMessageKey::getMessageKeyId, SysMessageKey::getMessageKey)
            .orderByDesc(SysMessageKey::getCreateTime)
            .list();
        return R.ok(list);
    }

    /**
     * 发送测试消息
     *
     * @param templateTestBo 测试对象
     * @return
     */
    @PostMapping("/sendTest")
    @SaCheckPermission(value = "system:messageTemplate:test")
    public R<Void> sendTest(@Validated @RequestBody SysMessageTemplateTestBo templateTestBo) {
        messageSendService.send(templateTestBo.getMessageTemplateId(), templateTestBo.getAccount(), templateTestBo.getVars());
        return R.ok();
    }

    /**
     * 刷新消息模板缓存
     */
    @SaCheckPermission("system:messageTemplate:remove")
    @Log(title = "消息模板", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        sysMessageTemplateService.removeCache();
        return R.ok();
    }

}
