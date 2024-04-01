package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BreedDTO {

    private UUID uuid;
    private String breedName;
    private UUID speciesUuid;
}
