package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.SpeciesDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Species;

import java.util.List;
import java.util.UUID;

public interface SpeciesService {

    Species saveSpecies(Species species);

    Species createSpecies(SpeciesDTO speciesDTO);

    Species updateSpecies(SpeciesDTO speciesDTO, UUID uuid);

    Species getSpeciesByUuid(UUID uuid);

    Species getSpeciesById(Long id);

    List<Species> getSpeciesByName(String speciesName);


    List<Species> getAllSpecies();

    List<Species> getSpeciesByBreed(String breed);

    void deleteSpecies(UUID uuid);
}
