package org.dromara.common.core.validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dromara.common.core.validation.PatternSplit;

import java.util.regex.Pattern;

/**
 * 对字符串分割后验证正则
 *
 * @author hexm
 * @date 2023/12/28
 */
public class PatternSplitValidator implements ConstraintValidator<PatternSplit, String> {
    private boolean required;
    private Pattern regexp;
    private Pattern split;

    /**
     * 初始化验证程序，以准备进行{@link #isValid（Object，ConstraintValidatorContext）}调用。
     * 给定约束声明的约束注释被传递。 <p>确保在将此实例用于验证之前先调用此方法。 <p>默认实现是无操作。
     *
     * @param constraintAnnotation 给定约束声明的注释实例
     */
    @Override
    public void initialize(PatternSplit constraintAnnotation) {
        required = constraintAnnotation.required();
        regexp = Pattern.compile(constraintAnnotation.regexp());
        split = Pattern.compile(constraintAnnotation.split());
    }

    /**
     * 实现验证逻辑。 {@code value}的状态不得更改。 <p>该方法可以并发访问，实现必须确保线程安全。
     *
     * @param value   验证对象
     * @param context 评估约束的上下文
     * @return {@code false} 如果{@code value}没有通过约束
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null && !required) {
            return true;
        } else if (value != null) {
            String[] strings = split.split(value);
            for (String str : strings) {
                if (!regexp.matcher(str).matches()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
