package com.Japkutija.veterinarybackend.veterinary.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vaccination")
@Data
public class Vaccination {

    @Id
    @NotNull
    @Column(name = "vaccination_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "vaccination_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;


    @Column(name = "vaccine_type", nullable = false, length = 20)
    @NotNull
    private String vaccineType;

    @Column(name = "date_of_vaccination", nullable = false)
    @NotNull
    private LocalDate dateOfVaccination;

    @Column(name = "validity", nullable = false)
    @NotNull
    private Date validity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull
    private Pet pet;

}
