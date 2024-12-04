package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByUuid(UUID uuid);
    
    List<Prescription> findByMedicalRecordUuid(UUID medicalRecordUuid);
    
    List<Prescription> findByMedicalRecordPetUuid(UUID petUuid);
    
    List<Prescription> findByIsActiveTrue();
}
