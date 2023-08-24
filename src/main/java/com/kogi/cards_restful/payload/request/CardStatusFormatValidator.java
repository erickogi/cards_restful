package com.kogi.cards_restful.payload.request;

import com.kogi.cards_restful.models.CardStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardStatusFormatValidator implements ConstraintValidator<CardStatusFormat, String> {
    @Override
    public void initialize(CardStatusFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are handled by @NotNull or other annotations
        }

        boolean isValid =  value.equals(CardStatus.DONE.name()) || value.equals(CardStatus.TODO.name()) || value.equals(CardStatus.IN_PROGRESS.name());

        System.out.println(isValid);
        return  isValid;
    }
}
