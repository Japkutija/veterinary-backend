package com.Japkutija.veterinarybackend.veterinary.model.entity;

import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.validation.DateRange;
import com.Japkutija.veterinarybackend.veterinary.validation.DosageFormat;
import com.Japkutija.veterinarybackend.veterinary.validation.FrequencyFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prescription")
@Data
@DateRange(message = "End date must be after start date")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id", nullable = false, unique = true)
    @NotNull
    private Long id;

    @Column(name = "prescription_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "medication", nullable = false, length = 255)
    @NotNull
    private String medication;

    @Column(name = "dosage", nullable = false, length = 255)
    @NotNull
    @DosageFormat
    private String dosage;

    @Column(name = "frequency", nullable = false, length = 255)
    @NotNull
    @FrequencyFormat
    private String frequency;

    @Column(name = "start_date", nullable = false)
    @NotNull
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull
    private LocalDate endDate;

    @Column(name = "instructions", columnDefinition = "TEXT")
    @NotNull
    private String instructions;

    @Column(name = "is_active", nullable = false)
    @NotNull
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    @NotNull
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    @NotNull
    private User veterinarian;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        // Ensuring the prescribing user is a veterinarian
        if (this.veterinarian != null && this.veterinarian.getRole() != Role.VETERINARIAN) {
            throw new IllegalStateException("Only veterinarians can prescribe medications");
        }
    }
}
