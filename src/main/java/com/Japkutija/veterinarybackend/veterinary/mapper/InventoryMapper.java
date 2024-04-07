package com.Japkutija.veterinarybackend.veterinary.mapper;

import com.Japkutija.veterinarybackend.veterinary.model.dto.InventoryDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    Inventory toInventory(InventoryDTO inventoryDTO);

    InventoryDTO toInventoryDTO(Inventory inventory);

    List<Inventory> toInventoryList(List<InventoryDTO> inventoryDTOs);

    List<InventoryDTO> toInventoryDTOList(List<Inventory> inventories);

    Inventory updateInventoryFromDto(InventoryDTO inventoryDTO, @MappingTarget Inventory inventory);
}

