package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.OwnerMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Page<Owner> getPaginatedAndSortedOwners(int pageIndex, int pageSize, String sortField, String sortOrder) {
        // Set the default sort direction if not provided
        var direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create the Sort object if sortField is provided, otherwise unsorted
        var sort = (sortField != null) ? Sort.by(direction, sortField) : Sort.unsorted();

        // Create pageRequest object with sorting and pagination
        var pageRequest = PageRequest.of(pageIndex - 1, pageSize, sort);

        // Fetch owners with pagination and sorting applied
        return ownerRepository.findAll(pageRequest);
    }

    @Override
    @Transactional
    public Owner updateOwner(UUID uuid, Owner ownerUpdates) {

        var existingOwner = getOwnerByUuid(uuid);

        ownerMapper.updateOwner(ownerUpdates, existingOwner);

        return ownerRepository.save(existingOwner);
    }

    @Override
    @Transactional
    public void deleteOwner(UUID uuid) {

        var owner = getOwnerByUuid(uuid);

        ownerRepository.delete(owner);

    }
}
