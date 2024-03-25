package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.service.OssService;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;

/**
 * OSS翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.OSS_ID_TO_URL)
public class OssUrlTranslationImpl extends SimpleTranslationImpl {

    private final OssService ossService;

    @Override
    public Object translation(Object key, Translation translation) {
        if (key instanceof String ids) {
            return ossService.selectUrlByIds(ids);
        } else if (key instanceof Long id) {
            return ossService.selectUrlByIds(id.toString());
        }
        return null;
    }
}
