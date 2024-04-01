package com.Japkutija.veterinarybackend.veterinary.model.entity;

import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "appointment")
public class Appointment {

    @Id
    @NotNull
    @Column(name = "appointment_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "appointment_date", nullable = false)
    @NotNull
    private Date appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    @NotNull
    private Instant appointmentTime;


    @Column(name = "reason", nullable = false, length = 255)
    @NotNull
    private String reason;

    @Column(name = "status", nullable = false, length = 50)
    @NotNull
    private AppointmentStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    @NotNull
    private Bill bill;


}
