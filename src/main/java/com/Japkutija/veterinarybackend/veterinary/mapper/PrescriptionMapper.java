package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.service.MedicalRecordService;
import com.Japkutija.veterinarybackend.veterinary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrescriptionMapper {

    private final MedicalRecordService medicalRecordService;
    private final UserService userService;

    public Prescription toEntity(PrescriptionDTO dto) {
        if (dto == null) {
            return null;
        }

        Prescription prescription = new Prescription();
        updatePrescriptionFromDto(dto, prescription);
        return prescription;
    }

    public PrescriptionDTO toDto(Prescription entity) {
        if (entity == null) {
            return null;
        }

        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setMedication(entity.getMedication());
        dto.setDosage(entity.getDosage());
        dto.setFrequency(entity.getFrequency());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setInstructions(entity.getInstructions());
        dto.setMedicalRecordUuid(entity.getMedicalRecord().getUuid());
        dto.setPrescribedByUuid(entity.getPrescribedBy().getUuid());
        return dto;
    }

    public Prescription updatePrescriptionFromDto(PrescriptionDTO dto, Prescription prescription) {
        if (dto == null) {
            return prescription;
        }

        prescription.setMedication(dto.getMedication());
        prescription.setDosage(dto.getDosage());
        prescription.setFrequency(dto.getFrequency());
        prescription.setStartDate(dto.getStartDate());
        prescription.setEndDate(dto.getEndDate());
        prescription.setInstructions(dto.getInstructions());

        // Set related entities
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordByUuid(dto.getMedicalRecordUuid());
        User prescribedBy = userService.getUserByUuid(dto.getPrescribedByUuid());

        prescription.setMedicalRecord(medicalRecord);
        prescription.setPrescribedBy(prescribedBy);

        return prescription;
    }
}
