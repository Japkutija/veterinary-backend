package com.Japkutija.veterinarybackend.veterinary.model.dto;

import com.Japkutija.veterinarybackend.veterinary.model.enums.BillStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class BillDTO {

    private UUID uuid;
    private String billNumber;
    private Date dateOfIssue;
    private BigDecimal totalAmount;
    private BillStatus status;
    private UUID ownerUuid;
}
