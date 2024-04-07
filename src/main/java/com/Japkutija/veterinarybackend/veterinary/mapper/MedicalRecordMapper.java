package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    MedicalRecord toMedicalRecord(MedicalRecordDTO medicalRecordDTO);

    MedicalRecordDTO toMedicalRecordDTO(MedicalRecord medicalRecord);

    List<MedicalRecord> toMedicalRecordList(List<MedicalRecordDTO> medicalRecordDTOs);

    List<MedicalRecordDTO> toMedicalRecordDTOList(List<MedicalRecord> medicalRecords);

    
    MedicalRecord updateMedicalRecordFromDto(MedicalRecordDTO medicalRecordDTO, @MappingTarget MedicalRecord medicalRecord);
}

