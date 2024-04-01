package com.Japkutija.veterinarybackend.veterinary.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "owner")
@Data
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id", nullable = false, unique = true)
    @NotNull
    private Long id;

    @Column(name = "event_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;


    @Column(name = "email", length = 50)
    @Email
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "EMSO", length = 13, unique = true)
    @Size(min = 13, max = 13)
    private String EMSO;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Bill> bills = new ArrayList<>();

}
