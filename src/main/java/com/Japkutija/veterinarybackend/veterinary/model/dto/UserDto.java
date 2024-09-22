package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class UserDto {

    @NotNull(message = "UUID cannot be null")
    private final UUID uuid;

    @NotNull(message = "Username cannot be null")
    private final String username;

    @NotNull(message = "Email cannot be null")
    private final String email;

    @NotNull(message = "Role cannot be null")
    @Enumerated(EnumType.STRING)
    private final Role role;

}
