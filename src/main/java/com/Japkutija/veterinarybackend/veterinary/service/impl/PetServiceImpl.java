package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityDeletionException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.PetMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.PetDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.repository.PetRepository;
import com.Japkutija.veterinarybackend.veterinary.service.OwnerService;
import com.Japkutija.veterinarybackend.veterinary.service.SpeciesService;
import java.util.Random;
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
    private final SpeciesService speciesService;
    private final BreedServiceImpl breedService;
    private final OwnerService ownerService;
    private final Random random = new Random();

    @Override
    @Transactional
    public Pet createPet(PetDTO petDTO) {

        // Add a validation before creating
        if (petRepository.existsByOwner_UuidAndNicknameIgnoreCase(petDTO.getOwnerUuid(), petDTO.getNickname())) {
            throw new EntitySavingException(String.format("Pet with nickname '%s' already exists", petDTO.getNickname()));
        }
        var pet = petMapper.toPet(petDTO);
        var breed = breedService.getBreedByName(petDTO.getBreedName());
        var owner = ownerService.getOwnerByUuid(petDTO.getOwnerUuid());
        var species = speciesService.getSpeciesByName(petDTO.getSpeciesName());

        breed.setUuid(UUID.randomUUID());
        pet.setBreed(breed);
        pet.setOwner(owner);
        pet.setUuid(UUID.randomUUID());
        pet.setChipNumber(generateChipNumber());
        pet.setSpecies(species);

        return savePet(pet);
    }

    private String generateChipNumber() {
        var sb = new StringBuilder();
        for (var i = 0; i < 20; i++) {
            sb.append(this.random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public Pet savePet(Pet pet) {
        try {
            var result = petRepository.save(pet);
            log.info("Pet saved successfully: {}", result);
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

        // Create pageRequest object with sorting and pagination
        var pageRequest = PageRequest.of(pageIndex - 1, pageSize, sort);

        // Fetch pets with pagination and sorting applied
        return petRepository.findAll(pageRequest);
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
