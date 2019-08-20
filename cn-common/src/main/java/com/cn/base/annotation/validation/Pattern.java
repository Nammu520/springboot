//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PatternValidator.class)
public @interface Pattern {
    String regexp();

    String field() default "";

    Pattern.Flag[] flags() default {};

    String message() default "{902}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     *
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Pattern[] value();
    }

    /**
     *
     */
    enum Flag {
        UNIX_LINES(1),
        CASE_INSENSITIVE(2),
        COMMENTS(4),
        MULTILINE(8),
        DOTALL(32),
        UNICODE_CASE(64),
        CANON_EQ(128);

        private final int value;


        Flag(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
