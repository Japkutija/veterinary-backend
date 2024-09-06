package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.PetDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface PetService {

    Pet createPet(PetDTO petDTO);

    Pet savePet(Pet pet);

    Pet updatePet(PetDTO petDTO, UUID uuid);

    Pet getPetByUuid(UUID uuid);

    Pet getPetById(Long id);

    List<Pet> getAllPets();

    Page<Pet> getPaginatedPets(int pageIndex, int pageSize);

    List<Pet> getPetsByOwner(UUID ownerUuid);

    List<Pet> getPetsBySpecies(String species);

    List<Pet> getPetsByBreed(String breed);

    void deletePet(UUID uuid);
}
