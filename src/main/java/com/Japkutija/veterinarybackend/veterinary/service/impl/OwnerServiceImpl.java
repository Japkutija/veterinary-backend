package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.OwnerMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    @Transactional
    public Owner createOwner(OwnerDTO ownerDTO) {

        var owner = ownerMapper.toOwner(ownerDTO);

        return saveOwner(owner);
    }

    @Override
    @Transactional
    public Owner saveOwner(Owner owner) {
        try {
            return ownerRepository.save(owner);
        } catch (Exception ex) {
            log.error("Error saving owner: {}", ex.getMessage());
            throw new EntitySavingException(Owner.class, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Owner getOwnerByUuid(UUID uuid) {
        var owner = ownerRepository.findByUuid(uuid);
        return owner.orElseThrow(() -> new EntityNotFoundException(Owner.class, uuid));
    }


    @Override
    @Transactional(readOnly = true)
    public List<Owner> getAllOwners() {
        var owners = ownerRepository.findAll();

        if (owners.isEmpty()) {
            return List.of();
        } else {
            return owners;
        }
    }

    @Override
    @Transactional

    public Owner updateOwner(UUID uuid, OwnerDTO ownerDTO) {

        var owner = getOwnerByUuid(uuid);

        var updatedOwner = ownerMapper.updateOwnerFromDto(ownerDTO, owner);

        return ownerRepository.save(updatedOwner);
    }

    @Override
    @Transactional
    public void deleteOwner(UUID uuid) {

        var owner = getOwnerByUuid(uuid);

        ownerRepository.delete(owner);

    }
}
