package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.BreedMapper;
import com.Japkutija.veterinarybackend.veterinary.mapper.PetMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.BreedDTO;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.PetWithSpeciesAndBreedDto;
import com.Japkutija.veterinarybackend.veterinary.service.BreedService;
import com.Japkutija.veterinarybackend.veterinary.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/pets")
@Tag(name = "CRUD Operations on Pets", description = "Pets API")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final PetMapper petMapper;

    @GetMapping("/{uuid}")
    @Operation(summary = "Get pet by uuid", description = "Returns the pet data for the given uuid", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found")})
    public ResponseEntity<PetWithSpeciesAndBreedDto> getPetByUuid(@PathVariable @NotNull UUID uuid) {

            var pet = petService.getPetByUuid(uuid);

            return ResponseEntity.ok(petMapper.toPetWithSpeciesAndBreedDto(pet));
    }

    // Retrieve all the pets (paginated)
    @GetMapping
    @Operation(summary = "Get all pets", description = "Returns all pets", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "404", description = "Pets not found")})
    public ResponseEntity<Page<PetWithSpeciesAndBreedDto>> getAllPets(@RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "10") int pageSize) {

            var pets = petService.getPaginatedPets(pageIndex, pageSize);

            return ResponseEntity.ok(pets.map(petMapper::toPetWithSpeciesAndBreedDto));

    }

}
