package com.Japkutija.veterinarybackend.veterinary.mapper;


import com.Japkutija.veterinarybackend.veterinary.model.dto.TreatmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    @Mapping(source = "petUuid", target = "pet.uuid")
    Treatment toTreatment(TreatmentDTO treatmentDTO);

    @Mapping(source = "pet.uuid", target = "petUuid")
    TreatmentDTO toTreatmentDTO(Treatment treatment);

    List<Treatment> toTreatmentList(List<TreatmentDTO> treatmentDTOs);

    List<TreatmentDTO> toTreatmentDTOList(List<Treatment> treatments);

    Treatment updateTreatmentFromDto(TreatmentDTO treatmentDTO, @MappingTarget Treatment treatment);
}

