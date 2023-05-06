package org.dromara.common.translation.core.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.common.translation.core.TranslationInterface;

import java.io.IOException;

/**
 * 实现简单的翻译赋值操作
 *
 * @author hexm
 * @date 2023/05/06 09:32
 */
public class SimpleTranslationImpl implements TranslationInterface {

    /**
     * 翻译
     *
     * @param key         需要被翻译的键(不为空)
     * @param translation 翻译注解
     * @param gen         json对象
     */
    @Override
    public void translation(Object key, Translation translation, JsonGenerator gen) throws IOException {
        Object value = translation(key, translation);
        gen.writeObject(value);
    }

    /**
     * 翻译
     *
     * @param key         需要被翻译的键(不为空)
     * @param translation 翻译注解
     */
    protected Object translation(Object key, Translation translation) {
        return key;
    }
}
