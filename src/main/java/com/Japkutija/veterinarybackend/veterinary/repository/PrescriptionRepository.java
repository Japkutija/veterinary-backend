package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    void deleteByUuid(UUID uuid);

    Optional<Prescription> findByUuid(UUID uuid);

    Optional<List<Prescription>> findByMedicalRecordUuid(UUID medicalRecordUuid);


    @Query("SELECT p FROM Prescription p WHERE p.medicalRecord.pet.uuid = :petUuid AND p.isActive = true")
    Optional<List<Prescription>> findByMedicalRecord_Pet_UuidAndIsActiveIsTrue(@Param("petUuid") UUID petUuid);

    List<Prescription> findByMedicalRecordPetUuid(UUID petUuid);

}
