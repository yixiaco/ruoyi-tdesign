package org.dromara.common.core.validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.dromara.common.core.validation.AnyEnum;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 对字符串验证枚举
 *
 * @author hexm
 * @date 2020/5/10
 */
public class AnyEnumValidator implements ConstraintValidator<AnyEnum, Object> {
    /**
     * 枚举类
     */
    private Class<? extends Enum<?>>[] cls;
    private boolean required;
    private boolean ignoreCase;
    private String fieldName;

    /**
     * 初始化验证程序，以准备进行{@link #isValid（Object，ConstraintValidatorContext）}调用。
     * 给定约束声明的约束注释被传递。 <p>确保在将此实例用于验证之前先调用此方法。 <p>默认实现是无操作。
     *
     * @param constraintAnnotation 给定约束声明的注释实例
     */
    @Override
    public void initialize(AnyEnum constraintAnnotation) {
        cls = constraintAnnotation.value();
        required = constraintAnnotation.required();
        ignoreCase = constraintAnnotation.ignoreCase();
        fieldName = constraintAnnotation.field();
    }

    /**
     * 实现验证逻辑。 {@code value}的状态不得更改。 <p>该方法可以并发访问，实现必须确保线程安全。
     *
     * @param value   验证对象
     * @param context 评估约束的上下文
     * @return {@code false} 如果{@code value}没有通过约束
     */
    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null && !required) {
            return true;
        } else if (value != null) {
            for (Class<? extends Enum<?>> cl : cls) {
                //枚举类验证
                Enum<?>[] objs = cl.getEnumConstants();
                Field field = null;
                if (StringUtils.hasText(fieldName)) {
                    field = ReflectionUtils.findField(cl, fieldName);
                    if (field != null) {
                        field.setAccessible(true);
                    }
                }
                for (Enum<?> obj : objs) {
                    Object code;
                    if (field != null) {
                        code = field.get(obj);
                    } else {
                        code = obj.name();
                    }
                    if (value instanceof String str && ignoreCase) {
                        if (str.equalsIgnoreCase(code.toString())) {
                            return true;
                        }
                    } else if (Objects.equals(value, code)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
