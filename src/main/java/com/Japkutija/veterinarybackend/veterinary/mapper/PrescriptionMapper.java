package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(source = "medicalRecordUuid", target = "medicalRecord.uuid")
    Prescription toPrescription(PrescriptionDTO prescriptionDTO);

    @Mapping(source = "medicalRecord.uuid", target = "medicalRecordUuid")
    PrescriptionDTO toPrescriptionDTO(Prescription prescription);

    List<PrescriptionDTO> toPrescriptionDTOList(List<Prescription> prescriptions);
    
    List<Prescription> toPrescriptionList(List<PrescriptionDTO> prescriptionDTOs);

    @Mapping(source = "medicalRecordUuid", target = "medicalRecord.uuid")
    Prescription updatePrescriptionFromDTO(PrescriptionDTO prescriptionDTO, @MappingTarget Prescription prescription);
}