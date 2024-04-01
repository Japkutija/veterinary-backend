package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SpeciesDTO {

    private UUID uuid;
    private String speciesName;
}
