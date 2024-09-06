package com.Japkutija.veterinarybackend.veterinary.model.dto.response;

import com.Japkutija.veterinarybackend.veterinary.model.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import lombok.Data;

@Data
public class PetWithSpeciesAndBreedDto {

    @NotNull
    private UUID uuid;

    @NotNull
    @Size(max = 20)
    private String chipNumber;

    @NotNull
    @Size(max = 20)
    private String nickname;

    @NotNull
    private Gender gender;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private BigDecimal weight;

    @NotNull
    private BigDecimal height;

    @NotNull
    private String ownerName;

    @NotNull
    private String speciesName;

    @NotNull
    private String breedName;
}