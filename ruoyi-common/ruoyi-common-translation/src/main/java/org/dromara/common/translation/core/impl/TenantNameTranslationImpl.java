package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.service.TenantService;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 租户企业名称翻译实现
 *
 * @author hexm
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.TENANT_ID_TO_NAME)
public class TenantNameTranslationImpl extends SimpleTranslationImpl {

    private final TenantService tenantService;

    @Override
    public Object translation(Object key, Translation translation) {
        if (key instanceof String id) {
            return tenantService.selectTenantNameById(id);
        }
        return null;
    }
}
