package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.InventoryMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.InventoryDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Inventory;
import com.Japkutija.veterinarybackend.veterinary.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements com.Japkutija.veterinarybackend.veterinary.service.InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;


    @Override
    @Transactional
    public Inventory createInventory(InventoryDTO inventoryDTO) {

        var inventory = inventoryMapper.toInventory(inventoryDTO);

        return saveInventory(inventory);
    }

    @Override
    @Transactional
    public Inventory saveInventory(Inventory inventory) {

        try {
            return inventoryRepository.save(inventory);
        } catch (Exception ex) {
            log.error("Error saving inventory: {}", ex.getMessage());
            throw new EntitySavingException(Inventory.class, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventoryByUuid(UUID uuid) {

        var inventory = inventoryRepository.findByUuid(uuid);

        return inventory.orElseThrow(() -> new EntityNotFoundException(Inventory.class, uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventories() {

        var inventories = inventoryRepository.findAll();

        if (inventories.isEmpty()) {
            return List.of();
        }
        return inventories;
    }

    @Override
    @Transactional
    public Inventory updateInventory(UUID uuid, InventoryDTO inventoryDTO) {

        var inventory = getInventoryByUuid(uuid);

        var updatedInventory = inventoryMapper.updateInventoryFromDto(inventoryDTO, inventory);

        return inventoryRepository.save(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(UUID uuid) {

        var inventory = getInventoryByUuid(uuid);

        inventoryRepository.delete(inventory);
    }
}
