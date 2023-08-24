package com.kogi.cards_restful.payload.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CardColorFormatValidator.class)
@Documented
public @interface CardColorFormat {
    String message() default "Invalid format. Should be 6 alphanumeric characters prefixed with #";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}