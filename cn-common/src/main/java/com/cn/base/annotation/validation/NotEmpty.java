package com.cn.base.annotation.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmpty {

    String field() default "";

    String message() default "{err.msg.common.field.not.empty}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
