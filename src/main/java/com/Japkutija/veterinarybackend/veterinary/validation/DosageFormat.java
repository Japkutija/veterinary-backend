package com.Japkutija.veterinarybackend.veterinary.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DosageFormatValidator.class)
@Documented
public @interface DosageFormat {
    String message() default "Invalid dosage format. Expected format: amount + unit (e.g., '50mg', '2 pills', '10ml')";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
