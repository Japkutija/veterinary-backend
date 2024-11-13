package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.BreedDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Breed;

import java.util.List;
import java.util.UUID;

public interface BreedService {

    List<Breed> getBreedsBySpeciesName(String speciesName);
    Breed createBreed(BreedDTO breedDTO);

    Breed saveBreed(Breed breed);

    Breed getBreedByUuid(UUID uuid);

    Breed getBreedByName(String name);

    List<Breed> getAllBreeds();

    List<Breed> getBreedsBySpeciesUuid(UUID speciesUuid);


    Breed updateBreed(UUID uuid, BreedDTO breedDTO);

    void deleteBreed(UUID uuid);


}
