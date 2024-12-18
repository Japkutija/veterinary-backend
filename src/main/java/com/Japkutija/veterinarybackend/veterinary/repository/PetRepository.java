package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> findByUuid(UUID uuid);

    List<Pet> findByOwnerUuid(UUID ownerUuid);

    List<Pet> findByBreedBreedName(String breed);

    void deleteByUuid(UUID uuid);

    List<Pet> findBySpeciesSpeciesName(String species);

    boolean existsByOwner_UuidAndNicknameIgnoreCase(UUID ownerUuid, String nickname);
}
