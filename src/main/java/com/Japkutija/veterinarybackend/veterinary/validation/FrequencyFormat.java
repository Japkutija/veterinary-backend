package com.Japkutija.veterinarybackend.veterinary.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FrequencyFormatValidator.class)
@Documented
public @interface FrequencyFormat {
    String message() default "Invalid frequency format. Expected formats: 'X times daily', 'every X hours', 'once daily', 'twice daily'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
