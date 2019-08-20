package com.cn.base.annotation.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {

    /**
     * @param annotation annotation
     */
    @Override
    public void initialize(NotEmpty annotation) {
    }

    /**
     * @param str                        str
     * @param constraintValidatorContext constraintValidatorContext
     * @return boolean
     */
    @Override
    public boolean isValid(Object str, ConstraintValidatorContext constraintValidatorContext) {
        if (str != null) {
            return StringUtils.isNotEmpty(str.toString());
        }
        return true;
    }
}
