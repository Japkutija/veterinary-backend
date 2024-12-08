package com.Japkutija.veterinarybackend.veterinary.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class FrequencyFormatValidator implements ConstraintValidator<FrequencyFormat, String> {

    private static final Pattern FREQUENCY_PATTERN = Pattern.compile(
        "^(once daily|twice daily|three times daily|\\d+ times daily|every \\d+ hours?)$",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public boolean isValid(String frequency, ConstraintValidatorContext context) {
        if (frequency == null) {
            return true; // null validation is handled by @NotNull
        }
        return FREQUENCY_PATTERN.matcher(frequency.trim()).matches();
    }
}
