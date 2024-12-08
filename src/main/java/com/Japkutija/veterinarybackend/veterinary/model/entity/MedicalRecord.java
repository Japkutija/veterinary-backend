package com.Japkutija.veterinarybackend.veterinary.model.entity;

import com.Japkutija.veterinarybackend.veterinary.model.enums.MedicalRecordStatus;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "medical_record")
@Data
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    @NotNull
    private User veterinarian;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status_id")
    private MedicalRecordStatus status;

    @Column(name = "lab_results")
    private String labResults;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "notes")
    private String notes;


    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = MedicalRecordStatus.OPEN;
        }
        // Ensure the user creating the medical record is a veterinarian
        if (this.veterinarian != null && this.veterinarian.getRole() != Role.VETERINARIAN) {
            throw new IllegalStateException("Only veterinarians can create medical records");
        }
    }


}
