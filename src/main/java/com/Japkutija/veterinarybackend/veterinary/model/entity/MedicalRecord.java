package com.Japkutija.veterinarybackend.veterinary.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_record_id", nullable = false, unique = true)
    @NotNull
    private Long id;

    @Column(name = "medical_record_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "description", nullable = false, length = 255)
    @NotNull
    private String description;

    @Column(name = "visit_date", nullable = false)
    @NotNull
    private Date visitDate;


    @Column(name = "treatment_discipline", nullable = false, length = 255)
    @NotNull
    private String treatmentDiscipline;

    @Column(name = "follow_up_required", nullable = false)
    @NotNull
    private boolean followUpRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull
    private Pet pet;


}
