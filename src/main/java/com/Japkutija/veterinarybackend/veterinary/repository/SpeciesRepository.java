package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    Optional<Species> findByUuid(UUID uuid);

    List<Species> findBySpeciesName(String speciesName);

    List<Species> findByBreedsBreedName(String breed);
}
