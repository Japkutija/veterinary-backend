package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    Optional<Vaccination> findByUuid(UUID vaccinationUUID);

    List<Vaccination> findByPetUuid(UUID petUUID);
}
