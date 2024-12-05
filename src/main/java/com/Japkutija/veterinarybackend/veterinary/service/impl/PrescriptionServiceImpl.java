package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
import com.Japkutija.veterinarybackend.veterinary.mapper.PrescriptionMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.MedicalRecordRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.PrescriptionRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;

    private static final String MESSAGE_ENTITY_NOT_FOUND = "Prescription not found with UUID: ";

    @Override
    @Transactional
    public Prescription createPrescription(@Valid PrescriptionDTO prescriptionDTO) {
        var prescription = prescriptionMapper.toPrescription(prescriptionDTO);

        var medicalRecord = medicalRecordRepository.findByUuid(prescriptionDTO.getMedicalRecordUuid())
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with UUID: " + prescriptionDTO.getMedicalRecordUuid()));
        prescription.setMedicalRecord(medicalRecord);

        var veterinarian = userRepository.findByUuid(prescriptionDTO.getVeterinarianUuid())
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with UUID: " + prescriptionDTO.getVeterinarianUuid()));

        if (veterinarian.getRole() != Role.VETERINARIAN) {
            throw new IllegalStateException("Only veterinarians can create prescriptions");
        }
        prescription.setVeterinarian(veterinarian);
        prescription.setUuid(UUID.randomUUID());

        return savePrescription(prescription);
    }

    public Prescription savePrescription(Prescription prescription) {
        try {
            var result = prescriptionRepository.save(prescription);
            log.info("Prescription saved successfully: {}", result);
            return result;
        } catch (Exception ex) {
            log.error("Error saving prescription: {}", ex.getMessage());
            throw new EntityNotFoundException("Failed to save prescription", ex);
        }
    }

    @Override
    @Transactional
    public Prescription updatePrescription(PrescriptionDTO prescriptionDTO, UUID uuid) {
        var existingPrescription = prescriptionRepository.findByUuid(uuid)

                .orElseThrow(() -> new EntityNotFoundException(MESSAGE_ENTITY_NOT_FOUND + uuid));

        if (prescriptionDTO.getVeterinarianUuid() == null) {
            prescriptionMapper.updatePrescriptionFromDTO(prescriptionDTO, existingPrescription);
            return savePrescription(existingPrescription);
        }

        var veterinarian = userRepository.findByUuid(prescriptionDTO.getVeterinarianUuid())
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with UUID: " + prescriptionDTO.getVeterinarianUuid()));

        if (veterinarian.getRole() != Role.VETERINARIAN) {
            throw new IllegalStateException("Only veterinarians can be assigned to prescriptions");
        }

        prescriptionMapper.updatePrescriptionFromDTO(prescriptionDTO, existingPrescription);
        return savePrescription(existingPrescription);
    }

    @Override
    @Transactional
    public Prescription deactivatePrescription(UUID uuid) {
        var prescription = prescriptionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE_ENTITY_NOT_FOUND + uuid));
        prescription.setActive(false);
        try {
            savePrescription(prescription);
        } catch (Exception ex) {
            throw new GeneralRunTimeException("Failed to deactivate prescription", ex);
        }
        log.info("Prescription deactivated successfully: {}", uuid);
        return prescription;
    }

    @Override
    @Transactional(readOnly = true)
    public Prescription getPrescriptionByUuid(UUID uuid) {
        return prescriptionRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(MESSAGE_ENTITY_NOT_FOUND + uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionsByMedicalRecordUuid(UUID medicalRecordUuid) {
        return prescriptionRepository.findByMedicalRecordUuid(medicalRecordUuid)
                .orElse(List.of());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Prescription> getActivePrescriptionsByPetUuid(UUID petUuid) {
        return prescriptionRepository.findByMedicalRecord_Pet_UuidAndIsActiveIsTrue(petUuid)
                .orElse(List.of());
    }

    @Override
    @Transactional
    public void deletePrescription(UUID uuid) {
        try {
            prescriptionRepository.deleteByUuid(uuid);
        } catch (Exception e) {
            throw new GeneralRunTimeException("Failed to delete prescription", e);
        }
        log.info("Prescription deleted successfully: {}", uuid);
    }
}
