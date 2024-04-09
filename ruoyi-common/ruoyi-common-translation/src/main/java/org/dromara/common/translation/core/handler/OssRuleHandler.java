package org.dromara.common.translation.core.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.service.OssRuleService;
import org.dromara.common.core.service.OssService;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.translation.annotation.OssRule;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * OSS规则处理
 *
 * @author hexm
 * @date 2024/03/18 14:57
 */
@Slf4j
public class OssRuleHandler extends JsonSerializer<Object> implements ContextualSerializer {
    private OssRule ossRule;
    private static final OssRuleService ossRuleService = SpringUtil.getBean(OssRuleService.class);
    private static final OssService ossService = SpringUtil.getBean(OssService.class);

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (gen.getCurrentValue() != null) {
            // 如果映射字段不为空 则取映射字段的值
            if (StringUtils.isNotBlank(ossRule.mapper())) {
                value = getMapperValue(gen.getCurrentValue(), ossRule.mapper());
            }
        }
        if (value != null) {
            try {
                String fieldName = gen.getOutputContext().getCurrentName();
                // 如果是id，则将id转为url
                if (value instanceof Number || (value instanceof String val && isIdMode(val))) {
                    value = ossService.selectUrlByIds(value.toString());
                }
                String value0 = value != null ? value.toString() : "";
                String join = ossRule.join();
                String[] rules = ossRule.value();
                boolean useDefault = ossRule.useDefault();
                OssRule.PackingMethod packingMethod = ossRule.packingMethod();
                String wrapName = ossRule.wrapName();

                Map<String, String> urls = ossRuleService.getUrls(fieldName, value0, rules, join, useDefault);
                if (urls == null || urls.isEmpty()) {
                    gen.writeObject(value);
                    return;
                }
                if (packingMethod == OssRule.PackingMethod.UNWRAPPED) {
                    // 默认字段值
                    gen.writeString(urls.remove(fieldName));
                    // 写入非默认规则
                    for (Map.Entry<String, String> entry : urls.entrySet()) {
                        String name = entry.getKey();
                        String url = entry.getValue();
                        gen.writeFieldName(name);
                        gen.writeString(url);
                    }
                } else if (packingMethod == OssRule.PackingMethod.WRAPPED) {
                    // 默认字段值
                    gen.writeString(urls.get(fieldName));
                    // 写入包装字段中
                    gen.writeFieldName(fieldName + wrapName);
                    gen.writeObject(urls);
                }
            } catch (Exception e) {
                gen.writeObject(value);
                log.error(e.getMessage(), e);
            }
        } else {
            gen.writeObject(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.equals(String.class, property.getType().getRawClass()) || Number.class.isAssignableFrom(property.getType().getRawClass())) {
            OssRule ossRule = property.getAnnotation(OssRule.class);
            if (Objects.nonNull(ossRule)) {
                this.ossRule = ossRule;
                return this;
            }
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    private static Object getMapperValue(Object currentValue, String mapper) {
        Field field;
        Method method;
        Class<?> clz = currentValue.getClass();
        try {
            String methodName = "get" + StringUtils.capitalize(mapper);
            if ((method = ReflectionUtils.findMethod(clz, methodName)) != null) {
                method.setAccessible(true);
                return method.invoke(currentValue);
            }
            if ((field = ReflectionUtils.findField(clz, mapper)) != null) {
                field.setAccessible(true);
                return field.get(currentValue);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 是否是id模式
     *
     * @return
     */
    private boolean isIdMode(String path) {
        return path.matches("^([0-9],?)+$");
    }
}
