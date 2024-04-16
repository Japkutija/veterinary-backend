package com.Japkutija.veterinarybackend.veterinary.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryDTO {

    @NotNull(message = "Inventory UUID is required")
    private UUID uuid;

    @NotNull(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Item description is required")
    private String itemDescription;

    @NotNull(message = "Quantity in stock is required")
    @Min(value = 0, message = "Quantity in stock must be greater than or equal to 0")
    private int quantityInStock;

    @NotNull(message = "Reorder level is required")
    private int reorderLevel;
}
