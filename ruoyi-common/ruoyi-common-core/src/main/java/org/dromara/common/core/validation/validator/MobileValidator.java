package org.dromara.common.core.validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dromara.common.core.validation.Mobile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码校验注解
 * null和空字符串被认为是有效的
 *
 * @author hexm
 * @date 2023/02/10 16:54
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    private static final Pattern PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        } else {
            Matcher m = PATTERN.matcher(value);
            return m.matches();
        }
    }
}
