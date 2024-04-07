package com.Japkutija.veterinarybackend.veterinary.model.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class OwnerDTO {

    private UUID uuid;
    private String firstName;
    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;

    List<UUID> petUuids;


}
