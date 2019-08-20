package com.cn.base.annotation.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

/**
 *
 */
@Slf4j
public class PatternValidator implements ConstraintValidator<Pattern, CharSequence> {
    private java.util.regex.Pattern pattern;

    /**
     * @param parameters parameters
     */
    public void initialize(Pattern parameters) {
        try {
            this.pattern = java.util.regex.Pattern.compile(parameters.regexp());
        } catch (PatternSyntaxException var8) {
            throw new IllegalArgumentException(var8);
        }
    }

    /**
     * @param value                      value
     * @param constraintValidatorContext constraintValidatorContext
     * @return boolean
     */
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        } else {
            Matcher m = this.pattern.matcher(value);
            return m.matches();
        }
    }
}
