package com.Japkutija.veterinarybackend.veterinary.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppointmentType {

    GENERAL_CHECKUP(30),
    VACCINATION(15),
    COMPLEX_EXAMINATION(45);

    private final int defaultDuration;

}
