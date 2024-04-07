package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.SpeciesDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Species;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {

    Species toSpecies(SpeciesDTO speciesDTO);

    SpeciesDTO toSpeciesDTO(Species species);

    List<Species> toSpeciesList(List<SpeciesDTO> speciesDTOs);

    List<SpeciesDTO> toSpeciesDTOList(List<Species> species);
}

