package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Data;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.UUID;

@Data
public class TreatmentDTO {

    private UUID uuid;
    private String diagnosis;
    private String treatmentDiscipline;
    private Date treatmentDate;
    private String outcome;
    private UUID petUuid;
}
