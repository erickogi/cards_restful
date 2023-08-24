package com.kogi.cards_restful.payload.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardColorFormatValidator implements ConstraintValidator<CardColorFormat, String> {
    @Override
    public void initialize(CardColorFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are handled by @NotNull or other annotations
        }

        // Check if the string has the correct format
        return value.matches("#[A-Za-z0-9]{6}");
    }
}
