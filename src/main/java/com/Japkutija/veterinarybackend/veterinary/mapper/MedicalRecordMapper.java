package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(source = "petUuid", target = "pet.uuid")
    @Mapping(source = "veterinarianUuid", target = "veterinarian.uuid")
    MedicalRecord toMedicalRecord(MedicalRecordDTO medicalRecordDTO);

    @Mapping(source = "pet.uuid", target = "petUuid")
    @Mapping(source = "veterinarian.uuid", target = "veterinarianUuid")
    MedicalRecordDTO toMedicalRecordDTO(MedicalRecord medicalRecord);

    List<MedicalRecordDTO> toMedicalRecordDTOList(List<MedicalRecord> medicalRecords);
    
    List<MedicalRecord> toMedicalRecordList(List<MedicalRecordDTO> medicalRecordDTOs);

    @Mapping(source = "petUuid", target = "pet.uuid")
    @Mapping(source = "veterinarianUuid", target = "veterinarian.uuid")
    MedicalRecord updateMedicalRecordFromDTO(MedicalRecordDTO medicalRecordDTO, @MappingTarget MedicalRecord medicalRecord);
}
