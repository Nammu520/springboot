package com.cn.base.annotation.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 */
public class NotBlankValidator implements ConstraintValidator<NotBlank, Object> {

    @Override
    public void initialize(NotBlank annotation) {
    }

    @Override
    public boolean isValid(Object str, ConstraintValidatorContext constraintValidatorContext) {
        if (str != null) {
            return StringUtils.isNotEmpty(str.toString());
        }
        return false;
    }
}
