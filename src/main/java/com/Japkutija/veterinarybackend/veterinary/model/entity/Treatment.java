package com.Japkutija.veterinarybackend.veterinary.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "treatment")
@Data
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id", nullable = false, unique = true)
    @NotNull
    private Long id;

    @Column(name = "treatment_uuid", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @NotNull
    private UUID uuid;

    @Column(name = "diagnosis", nullable = false, length = 255)
    @NotNull
    private String diagnosis;

    @Column(name = "treatment_discipline", nullable = false, length = 50)
    @NotNull
    private String treatmentDiscipline;

    @Column(name = "treatment_date", nullable = false)
    @NotNull
    private LocalDate treatmentDate;

    @Column(name = "outcome", nullable = false, length = 255)
    @NotNull
    private String outcome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull
    private Pet pet;


}
