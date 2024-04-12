package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.SpeciesMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.SpeciesDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Species;
import com.Japkutija.veterinarybackend.veterinary.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpeciesServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.SpeciesService {

    private final SpeciesRepository speciesRepository;
    private final SpeciesMapper speciesMapper;

    @Override
    @Transactional
    public Species saveSpecies(Species species) {
        try {
            return speciesRepository.save(species);
        } catch (Exception ex) {
            log.error("Error saving species: {}", ex.getMessage());
            throw new EntitySavingException(Species.class, ex);
        }
    }

    @Override
    @Transactional
    public Species createSpecies(SpeciesDTO speciesDTO) {
        var species = speciesMapper.toSpecies(speciesDTO);

        return saveSpecies(species);
    }

    @Override
    @Transactional
    public Species updateSpecies(SpeciesDTO speciesDTO, UUID uuid) {
        var species = getSpeciesByUuid(uuid);

        var updatedSpecies = speciesMapper.updateSpeciesFromDto(speciesDTO, species);

        return saveSpecies(updatedSpecies);
    }

    @Override
    @Transactional(readOnly = true)
    public Species getSpeciesByUuid(UUID uuid) {
        var species = speciesRepository.findByUuid(uuid);

        return species.orElseThrow(() -> new EntityNotFoundException(Species.class, uuid));

    }

    @Override
    @Transactional(readOnly = true)
    public Species getSpeciesById(Long id) {
        var species = speciesRepository.findById(id);

        return species.orElseThrow(() -> new EntityNotFoundException(Species.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Species> getSpeciesByName(String speciesName) {
        var species = speciesRepository.findBySpeciesName(speciesName);

        if (species.isEmpty())
            return List.of();

        return species;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Species> getAllSpecies() {
        var species = speciesRepository.findAll();

        if (species.isEmpty())
            return List.of();

        return species;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Species> getSpeciesByBreed(String breed) {
        var species = speciesRepository.findByBreedsBreedName(breed);

        if (species.isEmpty())
            return List.of();

        return species;
    }

    @Override
    @Transactional
    public void deleteSpecies(UUID uuid) {

        var species = getSpeciesByUuid(uuid);

        speciesRepository.delete(species);

    }
}
