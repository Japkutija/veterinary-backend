package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.PetMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.PetDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional
    public Pet createPet(PetDTO petDTO) {
        var pet = petMapper.toPet(petDTO);

        return savePet(pet);
    }

    @Override
    @Transactional
    public Pet savePet(Pet pet) {
        try {
            return petRepository.save(pet);
        } catch (Exception ex) {
            log.error("Error saving pet: {}", ex.getMessage());
            throw new EntitySavingException(Pet.class, ex);
        }
    }

    @Override
    @Transactional
    public Pet updatePet(PetDTO petDTO, UUID uuid) {
        var pet = getPetByUuid(uuid);

        petMapper.updatePetFromDto(petDTO, pet);

        return savePet(pet);
    }

    @Override
    @Transactional(readOnly = true)
    public Pet getPetByUuid(UUID uuid) {
        var pet = petRepository.findByUuid(uuid);

        return pet.orElseThrow(() -> new EntityNotFoundException(Pet.class, uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Pet getPetById(Long id) {
        var pet = petRepository.findById(id);

        return pet.orElseThrow(() -> new EntityNotFoundException(Pet.class, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {
        var pets = petRepository.findAll();

        if (pets.isEmpty())
            return List.of();

        return pets;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsByOwner(UUID ownerUuid) {
        var pets = petRepository.findByOwnerUuid(ownerUuid);

        if (pets.isEmpty())
            return List.of();

        return pets;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsBySpecies(String species) {
        var pets = petRepository.findBySpecies(species);

        if (pets.isEmpty())
            return List.of();

        return pets;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsByBreed(String breed) {
        var pets = petRepository.findByBreed(breed);

        if (pets.isEmpty())
            return List.of();

        return pets;
    }

    @Override
    @Transactional
    public void deletePet(UUID uuid) {

        var pet = getPetByUuid(uuid);
        petRepository.delete(pet);
    }
}
