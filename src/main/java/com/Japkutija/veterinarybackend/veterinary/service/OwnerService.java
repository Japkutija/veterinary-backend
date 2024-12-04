package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface OwnerService {

    @Transactional(readOnly = true)
    Page<Owner> getPaginatedAndSortedOwners(int pageIndex, int pageSize, String sortField, String sortOrder);

    Owner createOwner(OwnerDTO ownerDTO);

    Owner getOwnerByUuid(UUID uuid);

    Owner saveOwner(Owner owner);

    List<Owner> getAllOwners();

    Owner updateOwner(UUID uuid, Owner owner);

    void deleteOwner(UUID uuid);


}
