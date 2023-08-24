package com.kogi.cards_restful.payload.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CardStatusFormatValidator.class)
@Documented
public @interface CardStatusFormat {
    String message() default "Invalid format. Should be either To Do, In Progress and Done";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}