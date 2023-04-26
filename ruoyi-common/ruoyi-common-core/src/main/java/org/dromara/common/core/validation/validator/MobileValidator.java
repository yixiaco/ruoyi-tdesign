package org.dromara.common.core.validation.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dromara.common.core.validation.Mobile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码校验注解
 * null被认为是有效的
 *
 * @author hexm
 * @date 2023/02/10 16:54
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    private static final Pattern PATTERN = Pattern.compile("^0?(13[0-9]|14[0-9]|15[0-9]|16[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            Matcher m = PATTERN.matcher(value);
            return m.matches();
        }
    }
}
