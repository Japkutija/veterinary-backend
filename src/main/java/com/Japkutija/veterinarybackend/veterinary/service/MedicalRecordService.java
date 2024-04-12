package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;

import java.util.List;
import java.util.UUID;

public interface MedicalRecordService {

    MedicalRecord createMedicalRecord(MedicalRecordDTO medicalRecordDTO);

    MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord);

    MedicalRecord updateMedicalRecord(MedicalRecordDTO medicalRecordDTO, UUID uuid);

    MedicalRecord getMedicalRecordByUuid(UUID uuid);

    List<MedicalRecord> getMedicalRecordsByPetUuid(UUID petUuid);

    List<MedicalRecord> getAllMedicalRecords();

    void deleteMedicalRecordByUuid(UUID uuid);
}
