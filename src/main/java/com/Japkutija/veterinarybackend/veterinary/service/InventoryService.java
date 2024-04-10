package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.InventoryDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Inventory;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    Inventory createInventory(InventoryDTO inventoryDTO);

    Inventory saveInventory(Inventory inventory);

    Inventory getInventoryByUuid(UUID uuid);

    List<Inventory> getAllInventories();

    Inventory updateInventory(UUID uuid, InventoryDTO inventoryDTO);

    void deleteInventory(UUID uuid);





}
