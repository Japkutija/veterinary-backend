package com.Japkutija.veterinarybackend.veterinary.model.entity;

import com.Japkutija.veterinarybackend.veterinary.model.enums.BillStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "bill")
@Data
public class Bill {

    @Id
    @NotNull
    @Column(name = "bill_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_uuid", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @NotNull
    private UUID uuid;

    @Column(name = "bill_number", nullable = false, unique = true)
    @NotNull
    private String billNumber;

    @Column(name = "date_of_issue", nullable = false)
    @NotNull
    private Date dateOfIssue;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @NotNull
    private BillStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

}
