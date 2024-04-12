package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;

import java.util.List;
import java.util.UUID;

public interface OwnerService {

    Owner createOwner(OwnerDTO ownerDTO);

    Owner getOwnerByUuid(UUID uuid);

    Owner saveOwner(Owner owner);

    List<Owner> getAllOwners();

    Owner updateOwner(UUID uuid, OwnerDTO ownerDTO);

    void deleteOwner(UUID uuid);


}
