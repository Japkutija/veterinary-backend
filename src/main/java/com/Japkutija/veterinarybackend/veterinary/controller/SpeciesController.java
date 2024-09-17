package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.PetMapper;
import com.Japkutija.veterinarybackend.veterinary.mapper.SpeciesMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.SpeciesDTO;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.PetWithSpeciesAndBreedDto;
import com.Japkutija.veterinarybackend.veterinary.service.PetService;
import com.Japkutija.veterinarybackend.veterinary.service.SpeciesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/species")
@Tag(name = "CRUD Operations on Species", description = "Species API")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesMapper speciesMapper;
    private final SpeciesService speciesService;


    /*@Operation(summary = "Get all species", description = "Get all species")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all species",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @GetMapping
    public ResponseEntity<Page<PetWithSpeciesAndBreedDto>> getAllSpecies(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size) {
        var species = speciesService.getAllSpecies(page, size);

        return ResponseEntity.ok(species.map(speciesMapper::toPetWithSpeciesAndBreedDto));
    }*/

    /*@GetMapping
    @Operation(summary = "Get all pets", description = "Returns all pets", tags = {"pets"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pets found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PetWithSpeciesAndBreedDto.class))),
            @ApiResponse(responseCode = "404", description = "Pets not found")})
    public ResponseEntity<Page<SpeciesDTO>> getPaginatedAndSortedSpecies(
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder) {

        var species = speciesService.getPaginatedAndSortedSpecies(pageIndex, pageSize, sortField, sortOrder);

        return ResponseEntity.ok(pets.map(petMapper::toPetWithSpeciesAndBreedDto));
    }*/
    // get all species not paginated though
    @GetMapping
    @Operation(summary = "Get all species", description = "Returns all species", tags = {"species"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Species found",
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SpeciesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Species not found")})
    public ResponseEntity<List<SpeciesDTO>> getAllSpecies() {

        var species = speciesService.getAllSpecies();

        return ResponseEntity.ok(speciesMapper.toSpeciesDTOList(species));
    }

}
