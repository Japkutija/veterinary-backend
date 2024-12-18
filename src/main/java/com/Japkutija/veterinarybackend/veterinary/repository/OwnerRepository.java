package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUuid(UUID uuid);

    boolean existsByEMSO(String emso);

    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT o FROM Owner o WHERE o.EMSO = :emso OR o.phoneNumber = :phoneNumber")
    List<Owner> findByEMSOOrPhoneNumber(@Param("emso") String emso, @Param("phoneNumber") String phoneNumber);
}
