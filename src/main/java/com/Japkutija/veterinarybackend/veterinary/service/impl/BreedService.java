package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.BreedMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.BreedDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Breed;
import com.Japkutija.veterinarybackend.veterinary.repository.BreedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BreedService implements com.Japkutija.veterinarybackend.veterinary.service.BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;

    @Override
    public Breed createBreed(BreedDTO breedDTO) {
        var breed = breedMapper.toBreed(breedDTO);

        return saveBreed(breed);
    }

    @Override
    public Breed saveBreed(Breed breed) {

        try {
            return breedRepository.save(breed);
        } catch (Exception ex) {
            log.error("Error saving breed: {}", ex.getMessage());
            throw new EntitySavingException(Breed.class, ex);
        }

    }

    @Override
    public Breed getBreedByUuid(UUID uuid) {
        var breed = breedRepository.findByUuid(uuid);

        return breed.orElseThrow(() -> new EntityNotFoundException(Breed.class, uuid));
    }

    @Override
    public List<Breed> getAllBreeds() {
        var breeds = breedRepository.findAll();

        if (breeds.isEmpty()) {
            return List.of();
        }
        return breeds;
    }

    @Override
    public List<Breed> getBreedsBySpeciesUuid(UUID speciesUuid) {
        var breeds = breedRepository.findAllBySpeciesUuid(speciesUuid);

        if (breeds.isEmpty()) {
            return List.of();
        }

        return breeds;
    }

    @Override
    public Breed updateBreed(UUID uuid, BreedDTO breedDTO) {

        var breed = getBreedByUuid(uuid);

        var updatedBreed = breedMapper.updateBreedFromDto(breedDTO, breed);

        return saveBreed(updatedBreed);

    }

    @Override
    public void deleteBreed(UUID uuid) {

        var breed = getBreedByUuid(uuid);

        breedRepository.delete(breed);

    }
}
