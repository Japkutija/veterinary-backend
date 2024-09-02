package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.InventoryMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.InventoryDTO;
import com.Japkutija.veterinarybackend.veterinary.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory API")
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;

    @Operation(summary = "Create a new inventory", description = "Creates a new inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
        var createdInventory = inventoryService.createInventory(inventoryDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryMapper.toInventoryDTO(createdInventory));
    }

    @Operation(summary = "Update an existing inventory", description = "Updates an existing inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<InventoryDTO> updateInventory(
            @NotNull UUID uuid,
            @RequestBody @Valid InventoryDTO inventoryDTO) {
        var updatedInventory = inventoryService.updateInventory(uuid, inventoryDTO);

        return ResponseEntity.ok(inventoryMapper.toInventoryDTO(updatedInventory));
    }

    @Operation(summary = "Get an inventory by uuid", description = "Gets an inventory by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<InventoryDTO> getInventory(@NotNull UUID uuid) {
        var inventory = inventoryService.getInventoryByUuid(uuid);

        return ResponseEntity.ok(inventoryMapper.toInventoryDTO(inventory));
    }

    @Operation(summary = "Get all inventory", description = "Gets all inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getInventory() {
        var inventory = inventoryService.getAllInventories();

        return ResponseEntity.ok(inventoryMapper.toInventoryDTOList(inventory));
    }

    @Operation(summary = "Delete an inventory by uuid", description = "Deletes an inventory by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory deleted",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = InventoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteInventory(@NotNull UUID uuid) {

        inventoryService.deleteInventory(uuid);

        return ResponseEntity.ok().build();
    }
}
