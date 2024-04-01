package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MedicalRecordDTO {
    private UUID uuid;
    private String description;
    private Date visitDate;
    private String treatmentDiscipline;
    private boolean followUpRequired;
    private UUID petUuid;
}
