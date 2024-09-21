package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.OwnerMapper;
import com.Japkutija.veterinarybackend.veterinary.mapper.PetMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.OwnerDTO;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.PetWithSpeciesAndBreedDto;
import com.Japkutija.veterinarybackend.veterinary.service.OwnerService;
import com.Japkutija.veterinarybackend.veterinary.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/owners")
@Tag(name = "CRUD Operations on Owners", description = "Owners API")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    /*@GetMapping("/{uuid}")
    @Operation(summary = "Get pet by uuid", description = "Returns the pet data for the given uuid", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found")})
    public ResponseEntity<PetWithSpeciesAndBreedDto> getPetByUuid(@PathVariable @NotNull UUID uuid) {

        var pet = petService.getPetByUuid(uuid);

        return ResponseEntity.ok(petMapper.toPetWithSpeciesAndBreedDto(pet));
    }*/

    /*@GetMapping
    @Operation(summary = "Get all pets", description = "Returns all pets", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "404", description = "Pets not found")})
    public ResponseEntity<Page<PetWithSpeciesAndBreedDto>> getAllPets(
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {

        var pets = petService.getPaginatedAndSortedPets(pageIndex, pageSize, sortField, sortOrder);

        return ResponseEntity.ok(pets.map(petMapper::toPetWithSpeciesAndBreedDto));
    }*/
    // Get all owners
    @GetMapping
    @Operation(summary = "Get all owners", description = "Returns all owners", tags = {"owners"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Owners found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "404", description = "Owners not found")})
    public ResponseEntity<Page<OwnerDTO>> getAllOwners(
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {

        var owners = ownerService.getPaginatedAndSortedOwners(pageIndex, pageSize, sortField, sortOrder);

        return ResponseEntity.ok(owners.map(ownerMapper::toOwnerDto));
    }

    /*@Operation(summary = "Delete a pet", description = "Deletes a pet by its uuid", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet deleted"),
            @ApiResponse(responseCode = "404", description = "Pet not found")})
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePet(@PathVariable @NotNull UUID uuid) {

        petService.deletePet(uuid);

        return ResponseEntity.noContent().build();
    }*/


    /*@Operation(summary = "Update a pet", description = "Updates a pet by its uuid", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet updated"),
            @ApiResponse(responseCode = "404", description = "Pet not found")})
    @PutMapping("/{uuid}")
    public ResponseEntity<PetWithSpeciesAndBreedDto> updatePet(@PathVariable @NotNull UUID uuid, @RequestBody PetWithSpeciesAndBreedDto petDto) {

        var updateEntity = petMapper.fromPetWithSpeciesAndBreedDto(petDto);

        var result = petService.updatePet(updateEntity, uuid);


        return ResponseEntity.ok(petMapper.toPetWithSpeciesAndBreedDto(result));
    }*/
    @PutMapping("/{uuid}")
    @Operation(summary = "Update owner by uuid", description = "Updates an owner by its uuid", tags = {"owners"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Owner updated"),
            @ApiResponse(responseCode = "404", description = "Owner not found")})
    public ResponseEntity<OwnerDTO> updateOwner(@PathVariable @NotNull UUID uuid, @RequestBody OwnerDTO ownerDTO) {

            var ownerUpdates = ownerMapper.toOwner(ownerDTO);

            var result = ownerService.updateOwner(uuid, ownerUpdates);

            return ResponseEntity.ok(ownerMapper.toOwnerDto(result));
    }
}
