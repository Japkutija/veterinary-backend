package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class VaccinationDTO {
    private UUID uuid;
    private String vaccineType;
    private Date dateOfVaccination;
    private Date validity;
    private UUID petUuid;
}
