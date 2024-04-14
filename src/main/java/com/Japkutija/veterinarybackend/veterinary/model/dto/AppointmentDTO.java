package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
public class AppointmentDTO {

    @NotNull(message = "UUID is required")
    private UUID uuid;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date must be in the present or future")
    private Date appointmentDate;

    @NotNull(message = "Appointment time is required")
    private Instant appointmentTime;

    @NotNull(message = "Reason is required")
    @Size(min = 10, max = 255, message = "Reason must be between 10 and 255 characters")
    private String reason;

    @NotNull(message = "Appointment status is required")
    private AppointmentStatus status;

    @NotNull(message = "Pet UUID is required")
    private UUID petUuid;

    @NotNull(message = "Owner UUID is required")
    private UUID ownerUuid;

    @NotNull(message = "Bill UUID is required")
    private UUID billUuid;
}
