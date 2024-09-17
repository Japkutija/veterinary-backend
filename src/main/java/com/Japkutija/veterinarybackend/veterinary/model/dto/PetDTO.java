package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class PetDTO {

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
    private UUID ownerUuid;

    @NotNull
    private UUID speciesUuid;

    @NotNull
    private String speciesName;

    @NotNull
    private String breedName;

    @NotNull
    private UUID breedUuid;
}