package com.Japkutija.veterinarybackend.veterinary.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class PrescriptionDTO {
    
    @NotBlank(message = "Medication name is required")
    private String medication;
    
    @NotBlank(message = "Dosage is required")
    private String dosage;
    
    @NotBlank(message = "Frequency is required")
    private String frequency;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private String instructions;
    
    @NotNull(message = "Medical record UUID is required")
    private UUID medicalRecordUuid;
    
    @NotNull(message = "Prescribing veterinarian UUID is required")
    private UUID prescribedByUuid;
}
