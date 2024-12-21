package com.Japkutija.veterinarybackend.veterinary.service.impl;

public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(String s) {}



    public AppointmentConflictException(String message, Exception ex) {
        super(message, ex);
    }
}
