package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByUuid(UUID uuid);

    Optional<Bill> findByBillNumber(String billNumber);

    List<Bill> findAllByOwnerUuid(UUID ownerUuid);
}
