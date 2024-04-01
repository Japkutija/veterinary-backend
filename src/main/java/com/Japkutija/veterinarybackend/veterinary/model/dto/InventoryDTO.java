package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryDTO {

    private UUID uuid;
    private String itemName;
    private String itemDescription;
    private int quantityInStock;
    private int reorderLevel;
}
