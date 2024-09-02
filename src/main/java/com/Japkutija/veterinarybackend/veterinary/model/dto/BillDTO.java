package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.BillStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class BillDTO {

    @NotNull(message = "UUID is required")
    private UUID uuid;

    @NotNull(message = "Bill number is required")
    private String billNumber;

    @FutureOrPresent(message = "Date of issue must be in the present or future")
    private Date dateOfIssue;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @NotNull(message = "Bill status is required")
    private BillStatus status;

    @NotNull(message = "Owner UUID is required")
    private UUID ownerUuid;
}
