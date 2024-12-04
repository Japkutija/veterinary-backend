package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;

import java.util.List;
import java.util.UUID;

public interface PrescriptionService {
    
    Prescription createPrescription(PrescriptionDTO prescriptionDTO);
    
    Prescription savePrescription(Prescription prescription);
    
    Prescription updatePrescription(PrescriptionDTO prescriptionDTO, UUID uuid);
    
    Prescription getPrescriptionByUuid(UUID uuid);
    
    List<Prescription> getPrescriptionsByMedicalRecordUuid(UUID medicalRecordUuid);
    
    List<Prescription> getPrescriptionsByPetUuid(UUID petUuid);
    
    List<Prescription> getActivePrescriptions();
    
    void deletePrescription(UUID uuid);
    
    Prescription deactivatePrescription(UUID uuid);
}
