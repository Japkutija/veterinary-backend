package com.Japkutija.veterinarybackend.veterinary.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})  // This annotation can be applied to classes
@Retention(RetentionPolicy.RUNTIME)  // Annotation will be available at runtime.
@Constraint(validatedBy = DateRangeValidator.class)  // Who does the validation?
@Documented
public @interface DateRange {
    String message() default "End date must be after start date";
    Class<?>[] groups() default {}; // Apply to all groups
    Class<? extends Payload>[] payload() default {};
}
