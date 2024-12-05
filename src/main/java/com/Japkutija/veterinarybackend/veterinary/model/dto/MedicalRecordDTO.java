package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.MedicalRecordStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MedicalRecordDTO {
    private UUID uuid;
    
    @NotNull(message = "Description is required")
    private String description;
    
    @NotNull(message = "Visit date is required")
    private Date visitDate;
    
    @NotNull(message = "Treatment discipline is required")
    private String treatmentDiscipline;
    
    @NotNull(message = "Follow up required flag is required")
    private boolean followUpRequired;
    
    @NotNull(message = "Pet UUID is required")
    private UUID petUuid;
    
    @NotNull(message = "Veterinarian UUID is required")
    private UUID veterinarianUuid;
    
    private MedicalRecordStatus status;
    private String labResults;
    
    @NotNull(message = "Diagnosis is required")
    private String diagnosis;
    
    private String notes;
}
