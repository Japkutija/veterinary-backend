package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.MedicalRecordMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import com.Japkutija.veterinarybackend.veterinary.repository.MedicalRecordRepository;
import com.Japkutija.veterinarybackend.veterinary.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecordDTO medicalRecordDTO) {
        var medicalRecord = medicalRecordMapper.toMedicalRecord(medicalRecordDTO);

        return saveMedicalRecord(medicalRecord);
    }

    @Override
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {

        try {
            return medicalRecordRepository.save(medicalRecord);
        } catch (Exception ex) {
            log.error("Error saving medical record: {}", ex.getMessage());
            throw new EntitySavingException(MedicalRecord.class, ex);
        }
    }

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecordDTO medicalRecordDTO, UUID uuid) {

        var medicalRecord = getMedicalRecordByUuid(uuid);
        var updatedMedicalRecord = medicalRecordMapper.updateMedicalRecordFromDto(medicalRecordDTO, medicalRecord);

        return saveMedicalRecord(updatedMedicalRecord);

    }

    @Override
    public MedicalRecord getMedicalRecordByUuid(UUID uuid) {
        var medicalRecord = medicalRecordRepository.findByUuid(uuid);

        return medicalRecord.orElseThrow(() -> new EntityNotFoundException(MedicalRecord.class, uuid));
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPetUuid(UUID petUuid) {
        var medicalRecords = medicalRecordRepository.findAllByPetUuid(petUuid);

        if (medicalRecords.isEmpty()) {
            return List.of();
        }

        return medicalRecords;
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        var medicalRecords = medicalRecordRepository.findAll();

        if (medicalRecords.isEmpty()) {
            return List.of();
        }

        return medicalRecords;
    }

    @Override
    public void deleteMedicalRecordByUuid(UUID uuid) {

        var medicalRecord = getMedicalRecordByUuid(uuid);
        medicalRecordRepository.delete(medicalRecord);

    }
}
