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
import org.springframework.web.context.request.RequestContextHolder;

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
public class OssRuleHandler extends JsonSerializer<String> implements ContextualSerializer {
    private OssRule ossRule;
    private static final OssRuleService ossRuleService = SpringUtil.getBean(OssRuleService.class);
    private static final OssService ossService = SpringUtil.getBean(OssService.class);

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (gen.getCurrentValue() != null) {
            // 如果映射字段不为空 则取映射字段的值
            if (StringUtils.isNotBlank(ossRule.mapper())) {
                value = getMapperValue(gen.getCurrentValue(), ossRule.mapper());
            }
        }
        if (StringUtils.isNotBlank(value)) {
            // 只在web环境下使用
            if (isWeb()) {
                String fieldName = gen.getOutputContext().getCurrentName();
                // 如果是id，则将id转为url
                if (isIdMode(value)) {
                    value = ossService.selectUrlByIds(value);
                }
                String join = ossRule.join();
                String[] rules = ossRule.value();
                boolean useDefault = ossRule.useDefault();
                OssRule.PackingMethod packingMethod = ossRule.packingMethod();
                String wrapName = ossRule.wrapName();

                Map<String, String> urls = ossRuleService.getUrls(fieldName, value, rules, join, useDefault);
                if (urls == null) {
                    gen.writeString(value);
                    return;
                }
                if (packingMethod == OssRule.PackingMethod.UNWRAPPED) {
                    // 默认字段值
                    gen.writeObject(urls.remove(fieldName));
                    // 写入非默认规则
                    for (Map.Entry<String, String> entry : urls.entrySet()) {
                        String name = entry.getKey();
                        String url = entry.getValue();
                        gen.writeFieldName(name);
                        gen.writeString(url);
                    }
                } else if (packingMethod == OssRule.PackingMethod.WRAPPED) {
                    // 默认字段值
                    gen.writeObject(urls.get(fieldName));
                    // 写入包装字段中
                    gen.writeFieldName(fieldName + wrapName);
                    gen.writeObject(urls);
                }
            } else {
                gen.writeObject(value);
            }
        } else {
            gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (Objects.equals(String.class, property.getType().getRawClass())) {
            OssRule ossRule = property.getAnnotation(OssRule.class);
            if (Objects.nonNull(ossRule)) {
                this.ossRule = ossRule;
                return this;
            }
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    private static String getMapperValue(Object currentValue, String mapper) {
        Field field;
        Method method;
        Class<?> clz = currentValue.getClass();
        try {
            String methodName = "get" + StringUtils.capitalize(mapper);
            if ((method = ReflectionUtils.findMethod(clz, methodName)) != null) {
                method.setAccessible(true);
                Object value0 = method.invoke(currentValue);
                if (value0 != null) {
                    return value0.toString();
                }
            }
            if ((field = ReflectionUtils.findField(clz, mapper)) != null) {
                field.setAccessible(true);
                Object value0 = field.get(currentValue);
                if (value0 != null) {
                    return value0.toString();
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 是否是web环境
     *
     * @return
     */
    private static boolean isWeb() {
        return RequestContextHolder.getRequestAttributes() != null;
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
