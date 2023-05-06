package org.dromara.common.translation.core.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.AllArgsConstructor;
import org.dromara.common.core.service.DeptService;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;

/**
 * 部门翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.DEPT_ID_TO_NAME)
public class DeptNameTranslationImpl extends SimpleTranslationImpl {

    private final DeptService deptService;

    @Override
    public Object translation(Object key, Translation translation) {
        if (key instanceof String ids) {
            return deptService.selectDeptNameByIds(ids);
        } else if (key instanceof Long id) {
            return deptService.selectDeptNameByIds(id.toString());
        }
        return null;
    }
}
