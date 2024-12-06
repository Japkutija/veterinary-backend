package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
import com.Japkutija.veterinarybackend.veterinary.mapper.MedicalRecordMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.MedicalRecordRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.PetRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;

    @Override
    public MedicalRecord createMedicalRecord(@Valid MedicalRecordDTO medicalRecordDTO) {
        var medicalRecord = medicalRecordMapper.toMedicalRecord(medicalRecordDTO);

        var pet = petRepository.findByUuid(medicalRecordDTO.getPetUuid())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with UUID: " + medicalRecordDTO.getPetUuid()));

        var veterinarian = userRepository.findByUuid(medicalRecordDTO.getVeterinarianUuid())
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with UUID: " + medicalRecordDTO.getVeterinarianUuid()));

        if (veterinarian.getRole() != Role.VETERINARIAN) {
            throw new IllegalStateException("Only veterinarians can create medical records");
        }
        medicalRecord.setVeterinarian(veterinarian);
        medicalRecord.setUuid(UUID.randomUUID());
        medicalRecord.setPet(pet);

        return applicationContext.getBean(MedicalRecordService.class).saveMedicalRecord(medicalRecord);
    }

    @Transactional
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        try {
            var result = medicalRecordRepository.save(medicalRecord);
            log.info("Medical record saved successfully: {}", result);
            return result;
        } catch (Exception ex) {
            log.error("Error saving medical record: {}", ex.getMessage());
            throw new EntityNotFoundException("Failed to save medical record", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecord getMedicalRecordByUuid(UUID uuid) {
        return medicalRecordRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with UUID: " + uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecord> getMedicalRecordsByPetUuid(UUID petUuid) {
        return medicalRecordRepository.findAllByPetUuid(petUuid)
                .orElseThrow(() -> new EntityNotFoundException("No medical records found for pet with UUID: " + petUuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    @Override
    @Transactional
    public MedicalRecord updateMedicalRecord(MedicalRecordDTO medicalRecordDTO, UUID uuid) {
        var existingRecord = medicalRecordRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with UUID: " + uuid));

        if(existingRecord.getVeterinarian().getRole() != Role.VETERINARIAN) {
            throw new IllegalStateException("Only veterinarians can update medical records");
        }
        var updatedRecord = medicalRecordMapper.updateMedicalRecordFromDTO(medicalRecordDTO, existingRecord);
        return applicationContext.getBean(MedicalRecordService.class).saveMedicalRecord(updatedRecord);
    }

    @Transactional
    @Override
    public void deleteMedicalRecordByUuid(UUID uuid) {
        try {
            medicalRecordRepository.deleteByUuid(uuid);
        } catch (Exception ex) {
            throw new GeneralRunTimeException("Failed to delete medical record", ex);
        }
        log.info("Medical record deleted successfully: {}", uuid);
    }
}
