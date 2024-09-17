package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
    Optional<Breed> findByUuid(UUID uuid);

    List<Breed> findAllBySpeciesUuid(UUID speciesUuid);
    List<Breed> findAllBySpecies_SpeciesName(String speciesName);
}
