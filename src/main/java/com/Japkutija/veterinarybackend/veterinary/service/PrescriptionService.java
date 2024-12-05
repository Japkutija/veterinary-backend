package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface PrescriptionService {
    
    Prescription createPrescription(PrescriptionDTO prescriptionDTO);
    
    Prescription savePrescription(Prescription prescription);
    
    Prescription updatePrescription(PrescriptionDTO prescriptionDTO, UUID uuid);
    
    Prescription getPrescriptionByUuid(UUID uuid);
    
    List<Prescription> getPrescriptionsByMedicalRecordUuid(UUID medicalRecordUuid);

    @Transactional(readOnly = true)
    List<Prescription> getActivePrescriptionsByPetUuid(UUID petUuid);

    void deletePrescription(UUID uuid);
    
    Prescription deactivatePrescription(UUID uuid);
}
