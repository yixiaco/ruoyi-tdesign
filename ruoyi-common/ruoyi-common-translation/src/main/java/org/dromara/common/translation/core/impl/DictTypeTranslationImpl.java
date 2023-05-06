package org.dromara.common.translation.core.impl;

import lombok.AllArgsConstructor;
import org.dromara.common.core.service.DictService;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;

/**
 * 字典翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl extends SimpleTranslationImpl {

    private final DictService dictService;

    @Override
    public Object translation(Object key, Translation translation) {
        if (key instanceof String dictValue && StringUtils.isNotBlank(translation.other())) {
            return dictService.getDictLabel(translation.other(), dictValue);
        }
        return null;
    }
}
