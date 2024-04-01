package com.Japkutija.veterinarybackend.veterinary.model.entity;

import com.Japkutija.veterinarybackend.veterinary.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pet")
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id", nullable = false, unique = true)
    @NotNull
    private Long id;

    @Column(name = "pet_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "chip_number", nullable = false, length = 20, unique = true)
    @NotNull
    private String chipNumber;

    @Column(name = "nickname", nullable = false, length = 20)
    @NotNull
    private String nickname;

    /*@Column(name = "species", nullable = false, length = 50)
    @NotNull
    private String species;*/

    /*@Column(name = "breed", nullable = false, length = 50)
    @NotNull
    private String breed;
*/

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    @NotNull
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull
    private Date dateOfBirth;

    @Column(name = "weight", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal weight;

    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal height;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull
    private Owner owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vaccination> vaccinations = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "species_id", nullable = false)
    @NotNull
    private Species species;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "breed_id", nullable = false)
    @NotNull
    private Breed breed;


}
