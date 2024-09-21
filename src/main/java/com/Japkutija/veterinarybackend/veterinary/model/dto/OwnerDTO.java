package com.Japkutija.veterinarybackend.veterinary.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OwnerDTO {

    private UUID uuid;
    private String firstName;
    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

    private String emso;

    List<UUID> petUuids;


}
