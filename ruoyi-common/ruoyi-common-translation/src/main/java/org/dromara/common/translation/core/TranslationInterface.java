package org.dromara.common.translation.core;

import com.fasterxml.jackson.core.JsonGenerator;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.annotation.TranslationType;

import java.io.IOException;

/**
 * 翻译接口 (实现类需标注 {@link TranslationType} 注解标明翻译类型)
 *
 * @author Lion Li
 */
public interface TranslationInterface {

    /**
     * 翻译
     *
     * @param key         需要被翻译的键(不为空)
     * @param translation 翻译注解
     * @param gen         json对象
     */
    void translation(Object key, Translation translation, JsonGenerator gen) throws IOException;
}
