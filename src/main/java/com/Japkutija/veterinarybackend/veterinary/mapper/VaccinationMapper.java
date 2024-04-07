package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.VaccinationDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Vaccination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VaccinationMapper {

    @Mapping(source = "pet.uuid", target = "petUuid")
    VaccinationDTO toVaccinationDTO(Vaccination vaccination);

    @Mapping(source = "petUuid", target = "pet.uuid")
    Vaccination toVaccination(VaccinationDTO vaccinationDTO);

    List<VaccinationDTO> toVaccinationDTOList(List<Vaccination> vaccinations);

    List<Vaccination> toVaccinationList(List<VaccinationDTO> vaccinationDTOs);

    Vaccination updateVaccinationFromDto(VaccinationDTO vaccinationDTO, @MappingTarget Vaccination vaccination);
}

