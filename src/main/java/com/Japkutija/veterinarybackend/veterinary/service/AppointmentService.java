package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.dto.AppointmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Appointment;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    Appointment createAppointment(AppointmentDTO appointmentDTO);

    Appointment getAppointmentByUuid(UUID uuid);

    Appointment saveAppointment(Appointment appointment);

    List<Appointment> getAllAppointments();

    Appointment updateAppointment(UUID uuid, AppointmentDTO appointmentDTO);

    void deleteAppointment(UUID uuid);

    List<Appointment> getAppointmentsByOwner(UUID ownerUuid);

    List<Appointment> getAppointmentsByPet(UUID petUuid);



}
