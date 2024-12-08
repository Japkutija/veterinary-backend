package com.Japkutija.veterinarybackend.veterinary.validation;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRange, Prescription> {
    
    @Override
    public void initialize(DateRange constraintAnnotation) {
        // Initialization if needed
    }

    @Override
    public boolean isValid(Prescription prescription, ConstraintValidatorContext context) {
        if (prescription.getStartDate() == null || prescription.getEndDate() == null) {
            return true; // Let @NotNull handle null validation
        }
        
        return prescription.getEndDate().isAfter(prescription.getStartDate());
    }
}
