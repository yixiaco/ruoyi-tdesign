package org.dromara.common.translation.core.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.AllArgsConstructor;
import org.dromara.common.core.service.OssRuleService;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.constant.TransConstant;
import org.dromara.common.translation.core.TranslationInterface;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.util.Map;

/**
 * oss规则翻译实现
 *
 * @author hexm
 * @date 2023/05/05 16:01
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.OSS_RULE)
public class OssRuleTranslationImpl implements TranslationInterface {

    private final OssRuleService ossRuleService;

    /**
     * 翻译
     *
     * @param key         需要被翻译的键(不为空)
     * @param translation 翻译注解
     * @param gen         json对象
     */
    @Override
    public void translation(Object key, Translation translation, JsonGenerator gen) throws IOException {
        // 只在web环境下使用
        if (key instanceof String && isWeb()) {
            String fieldName = gen.getOutputContext().getCurrentName();
            String[] useRules = StrUtil.isNotBlank(translation.other()) ? translation.other().split(",") : null;

            Map<String, String> urls = ossRuleService.getUrls(fieldName, (String) key, useRules);
            if (urls == null) {
                gen.writeObject(key);
                return;
            }
            // 默认字段值
            gen.writeObject(urls.remove(fieldName));
            // 写出非默认规则
            for (Map.Entry<String, String> entry : urls.entrySet()) {
                String name = entry.getKey();
                String url = entry.getValue();
                gen.writeFieldName(name);
                gen.writeString(url);
            }
        } else {
            gen.writeObject(key);
        }
    }

    /**
     * 是否是web环境
     *
     * @return
     */
    private static boolean isWeb() {
        return RequestContextHolder.getRequestAttributes() != null;
    }
}
