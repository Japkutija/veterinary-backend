package com.Japkutija.veterinarybackend.veterinary.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "species")
@Data
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "species_id", nullable = false, unique = true)
    @NotNull
    private Long id;

    @Column(name = "species_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "species_name", nullable = false, length = 50)
    @NotNull
    private String speciesName;

    @OneToMany(mappedBy = "species", fetch = FetchType.LAZY)
    private List<Breed> breeds;


}
