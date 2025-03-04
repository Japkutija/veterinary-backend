package com.Japkutija.veterinarybackend.veterinary.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppointmentType {

    GENERAL_CHECKUP(1, 30),
    VACCINATION(2, 15),
    COMPLEX_EXAMINATION(3, 45);

    private final int id;
    private final int defaultDuration;

    public static AppointmentType fromId(int id) {
        for (var type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown AppointmentType id: " + id);
    }
}
