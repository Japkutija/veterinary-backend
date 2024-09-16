package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.SpeciesDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Species;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

    Species toSpecies(SpeciesDTO speciesDTO);

    @Mapping(source = "speciesName", target = "name")
    SpeciesDTO toSpeciesDTO(Species species);

    List<Species> toSpeciesList(List<SpeciesDTO> speciesDTOs);

    List<SpeciesDTO> toSpeciesDTOList(List<Species> species);

    Species updateSpeciesFromDto(SpeciesDTO speciesDTO, @MappingTarget Species species);
}

