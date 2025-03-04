package com.Japkutija.veterinarybackend.veterinary.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MedicalRecordStatus {
    OPEN (1),
    IN_PROGRESS (2),
    COMPLETED (3),
    CLOSED (4);

    private final int id;

    public static MedicalRecordStatus fromId(int id) {
        for (var type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MedicalRecordStatus id: " + id);
    }
    }
