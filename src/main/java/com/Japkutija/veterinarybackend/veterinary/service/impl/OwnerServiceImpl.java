package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.OwnerService {

    private final OwnerRepository ownerRepository;

    @Override
    public Owner createOwner(OwnerDTO ownerDTO) {
        return null;
    }

    @Override
    public Owner getOwnerByUuid(UUID uuid) {

        Optional<Owner> owner = ownerRepository.findByUuid(uuid);

        return owner.orElseThrow(() -> new EntityNotFoundException(Owner.class, uuid));
    }

    @Override
    public List<Owner> getAllOwners() {
        return null;
    }

    @Override
    public Owner updateOwner(UUID uuid, OwnerDTO ownerDTO) {
        return null;
    }

    @Override
    public void deleteOwner(UUID uuid) {

    }
}
