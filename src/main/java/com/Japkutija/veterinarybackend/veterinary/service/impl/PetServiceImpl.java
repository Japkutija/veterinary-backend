package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityDeletionException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.PetMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.PetDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Pet savePet(Pet pet) {
        try {
            log.info("Attempting to save pet species name: {}", pet.getSpecies().getSpeciesName());
            log.info("Attempting to save pet breed name: {}", pet.getBreed().getBreedName());
            var result = petRepository.save(pet);
            return result;
        } catch (Exception ex) {
            log.error("Error saving pet: {}", ex.getMessage());
            throw new EntitySavingException(Pet.class, ex);
        }
    }

    @Override
    @Transactional
    public Pet updatePet(PetDTO petDTO, UUID uuid) {
        var pet = getPetByUuid(uuid);

        var result = petMapper.updatePetFromDto(petDTO, pet);

        log.info("Trying to save pet with species and breed: {} {}", result.getSpecies().getSpeciesName(), result.getBreed().getBreedName());
        return savePet(result);

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

        if (pets.isEmpty()) {
            return List.of();
        }

        return pets;
    }

    /*@Override
    @Transactional(readOnly = true)
    public Page<Pet> getAllPets(int page, int size) {
        return petRepository.findAll(PageRequest.of(page, size));
    }*/

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> getPaginatedAndSortedPets(int pageIndex, int pageSize, String sortField, String sortOrder) {
        // Set the default sort direction if not provided
        var direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create the Sort object if sortField is provided, otherwise unsorted
        var sort = (sortField != null) ? Sort.by(direction, sortField) : Sort.unsorted();

        // Create pageable object with sorting and pagination
        var pageable = PageRequest.of(pageIndex - 1, pageSize, sort);

        // Fetch pets with pagination and sorting applied
        return petRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsByOwner(UUID ownerUuid) {
        var pets = petRepository.findByOwnerUuid(ownerUuid);

        if (pets.isEmpty()) {
            return List.of();
        }

        return pets;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsBySpecies(String speciesName) {
        var pets = petRepository.findBySpeciesSpeciesName(speciesName);

        if (pets.isEmpty()) {
            return List.of();
        }

        return pets;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsByBreed(String breed) {
        var pets = petRepository.findByBreedBreedName(breed);

        if (pets.isEmpty()) {
            return List.of();
        }

        return pets;
    }

    @Override
    @Transactional
    public void deletePet(UUID uuid) {
        log.info("Attempting to delete pet with UUID: {}", uuid);
        try {
            var pet = getPetByUuid(uuid);
            petRepository.delete(pet);
            log.info("Successfully deleted pet with UUID: {}", uuid);
        } catch (Exception ex) {
            log.error("Error occurred while deleting pet with UUID: {} {}", uuid, ex.getMessage());
            throw new EntityDeletionException(Pet.class, "Error occurred while deleting pet", ex);
        }
    }
}
