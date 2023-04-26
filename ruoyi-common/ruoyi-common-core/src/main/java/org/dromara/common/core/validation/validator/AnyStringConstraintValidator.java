package org.dromara.common.core.validation.validator;


import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dromara.common.core.validation.AnyString;

/**
 * 对属性中的值，判定是否在数组中的任意一个
 *
 * @author hexm
 * @date 2020/5/10
 */
public class AnyStringConstraintValidator implements ConstraintValidator<AnyString, String> {
    /**
     * 数组
     */
    private String[] array;

    /**
     * 初始化验证程序，以准备进行{@link #isValid（Object，ConstraintValidatorContext）}调用。
     * 给定约束声明的约束注释被传递。 <p>确保在将此实例用于验证之前先调用此方法。 <p>默认实现是无操作。
     *
     * @param constraintAnnotation 给定约束声明的注释实例
     */
    @Override
    public void initialize(AnyString constraintAnnotation) {
        array = constraintAnnotation.value();
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
        if (StrUtil.isNotEmpty(value)) {
            for (String s : array) {
                if (s.equals(value)) {
                    return true;
                }
            }
        }
        return true;
    }
}
