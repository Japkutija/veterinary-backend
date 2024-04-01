package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
public class AppointmentDTO {

    private UUID uuid;
    private Date appointmentDate;
    private Instant appointmentTime;
    private String reason;
    private AppointmentStatus status;
    private UUID petUuid;
    private UUID ownerUuid;
    private UUID billUuid;
}
