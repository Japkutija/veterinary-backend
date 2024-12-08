package com.Japkutija.veterinarybackend.veterinary.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class DosageFormatValidator implements ConstraintValidator<DosageFormat, String> {

    // Pattern to match: number followed by unit (mg, ml, pills, tablets, etc.)
    private static final Pattern DOSAGE_PATTERN = Pattern.compile(
        "^\\d+(\\.\\d+)?\\s*(mg|ml|g|pills?|tablets?|capsules?)$",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public boolean isValid(String dosage, ConstraintValidatorContext context) {
        if (dosage == null) {
            return true; // null validation is handled by @NotNull
        }
        return DOSAGE_PATTERN.matcher(dosage.trim()).matches();
    }
}
