package com.Japkutija.veterinarybackend.veterinary.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BreedDTO {

    @NotNull(message = "Breed UUID is required")
    private UUID uuid;

    @NotNull(message = "Breed name is required")
    private String breedName;

    @NotNull(message = "Species UUID is required")
    private UUID speciesUuid;
}
