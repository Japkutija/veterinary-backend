package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.BreedMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.BreedDTO;
import com.Japkutija.veterinarybackend.veterinary.service.BreedService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/breeds")
@Tag(name = "CRUD Operations on Breeds", description = "Breed API")
@RequiredArgsConstructor
public class BreedController {

    private final BreedService breedService;
    private final BreedMapper breedMapper;

    @Operation(summary = "Create a new breed", description = "Creates a new breed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Breed created",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = BreedDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<BreedDTO> createBreed(@Valid @RequestBody BreedDTO breedDTO) {
        var createdBreed = breedService.createBreed(breedDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(breedMapper.toBreedDto(createdBreed));
    }

    @Operation(summary = "Update an existing breed", description = "Updates an existing breed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Breed updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = BreedDTO.class))),
            @ApiResponse(responseCode = "404", description = "Breed not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<BreedDTO> updateBreed(@NotNull UUID uuid, @RequestBody @Valid BreedDTO breedDTO) {
        var updatedBreed = breedService.updateBreed(uuid, breedDTO);

        return ResponseEntity.ok(breedMapper.toBreedDto(updatedBreed));
    }

    @Operation(summary = "Get all breeds", description = "Get all breeds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Breeds retrieved")
    })
    @GetMapping
    public List<BreedDTO> getBreeds() {
        var breeds = breedService.getAllBreeds();

        return breedMapper.toBreedDTOList(breeds);
    }

    @Operation(summary = "Get a breed by UUID", description = "Gets a breed by UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Breed retrieved"),
            @ApiResponse(responseCode = "404", description = "Breed not found")
    })
    @GetMapping("/{uuid}")
    public BreedDTO getBreed(@PathVariable @NotNull UUID uuid) {
        var breed = breedService.getBreedByUuid(uuid);

        return breedMapper.toBreedDto(breed);
    }

    @Operation(summary = "Get a breed by species UUID", description = "Gets a breed by species UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Breed retrieved"),
            @ApiResponse(responseCode = "404", description = "Breed not found")
    })
    @GetMapping("/species/uuid/{speciesUuid}")
    public List<BreedDTO> getBreedsBySpeciesUuid(@PathVariable @NotNull UUID speciesUuid) {
        var breeds = breedService.getBreedsBySpeciesUuid(speciesUuid);

        return breedMapper.toBreedDTOList(breeds);
    }

    @Operation(summary = "Get a list of breeds by species name", description = "Gets a list of breeds by species name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Breed retrieved"),
            @ApiResponse(responseCode = "404", description = "Breed not found")
    })
    @GetMapping("/species/name/{speciesName}")
    public List<BreedDTO> getBreedsBySpeciesUuid(@PathVariable @NotNull String speciesName) {
        var breeds = breedService.getBreedsBySpeciesName(speciesName);

        return breedMapper.toBreedDTOList(breeds);
    }

    @Operation(summary = "Delete an existing breed", description = "Deletes an existing breed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Breed deleted"),
            @ApiResponse(responseCode = "404", description = "Breed not found")
    })

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteBreed(@NotNull @PathVariable UUID uuid) {
        breedService.deleteBreed(uuid);

        return ResponseEntity.ok().build();
    }
}
