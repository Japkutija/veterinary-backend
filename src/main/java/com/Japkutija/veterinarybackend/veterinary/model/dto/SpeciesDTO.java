package com.Japkutija.veterinarybackend.veterinary.model.dto;

import java.util.List;
import lombok.Data;

import java.util.UUID;

@Data
public class SpeciesDTO {

    private UUID uuid;
    private String name;
    private List<BreedDTO> breeds;
}
