package com.Japkutija.veterinarybackend.veterinary.model.dto.response;

import com.Japkutija.veterinarybackend.veterinary.model.dto.groups.OnCreate;
import com.Japkutija.veterinarybackend.veterinary.model.dto.groups.OnUpdate;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import lombok.Data;


@Data
public class PetWithSpeciesAndBreedDto {

    @NotNull(message = "UUID is required", groups = {OnUpdate.class})
    @Null(message = "UUID must be null", groups = {OnCreate.class})
    private UUID uuid;

    @NotNull
    @Size(max = 20)
    private String nickname;

    @NotNull
    private Gender gender;

    @NotNull
    @PastOrPresent(message = "Date of birth cannot be in the future.")
    private Date dateOfBirth;

    @NotNull
    private BigDecimal weight;

    @NotNull
    private BigDecimal height;

    @NotNull(message = "Owner UUID is required")
    private UUID ownerUuid;

    private String ownerName;

    @NotNull
    private String speciesName;

    @NotNull(message = "Breed name is required")
    private String breedName;

    @Override
    public String toString() {
        return "PetWithSpeciesAndBreedDto{\n" +
                "uuid=" + uuid + ",\n" +
                "nickname='" + nickname + "',\n" +
                "gender=" + gender + ",\n" +
                "dateOfBirth=" + dateOfBirth + ",\n" +
                "weight=" + weight + ",\n" +
                "height=" + height + ",\n" +
                "ownerName='" + ownerUuid + "',\n" +
                "speciesName='" + speciesName + "',\n" +
                "breedName='" + breedName + "'\n" +
                '}';
    }
}