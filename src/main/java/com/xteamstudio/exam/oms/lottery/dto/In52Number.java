package com.xteamstudio.exam.oms.lottery.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {In52NumberValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface In52Number {

    /**
     * see Java Validation.
     *
     * @return error message
     */
    String message() default "choose between range 1 to 52";

    /**
     * see Java Validation.
     *
     * @return no used
     */
    Class<?>[] groups() default {};

    /**
     * see Java Validation.
     *
     * @return no used
     */
    Class<? extends Payload>[] payload() default {};

}