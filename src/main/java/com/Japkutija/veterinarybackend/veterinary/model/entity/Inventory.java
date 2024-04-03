package com.Japkutija.veterinarybackend.veterinary.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "inventory_uuid", nullable = false, unique = true)
    @NotNull
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID uuid;

    @Column(name = "item_name", nullable = false, length = 50)
    @NotNull
    private String itemName;

    @Column(name = "item_description", nullable = false, length = 50)
    @NotNull
    private String itemDescription;

    @Column(name = "quantity_in_stock", nullable = false)
    @NotNull
    @Positive
    private int quantityInStock;

    @Column(name = "reorder_level", nullable = false)
    @NotNull
    @Positive
    private int reorderLevel;


}
