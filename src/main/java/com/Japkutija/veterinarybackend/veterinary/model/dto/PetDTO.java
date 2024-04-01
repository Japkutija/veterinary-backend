package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.Gender;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class PetDTO {

    private UUID uuid;
    private String chipNumber;
    private String nickname;
    private Gender gender;
    private Date dateOfBirth;
    private BigDecimal weight;
    private BigDecimal height;
    private UUID ownerUuid;
    private UUID speciesUuid;
    private UUID breedUuid;
}
