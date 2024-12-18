package com.Japkutija.veterinarybackend.veterinary.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationDto {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String confirmPassword;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Date of birth cannot be in the future")
    private LocalDate dateOfBirth;

    @NotBlank(message = "EMSO is required")
    @Size(min = 13, max = 13)
    private String emso;

    @NotBlank(message = "Phone number is required")
    @Size(min = 9, max = 20)
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 255)
    private String address;


}
