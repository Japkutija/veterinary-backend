package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.mapper.AppointmentMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.AppointmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Appointment;
import com.Japkutija.veterinarybackend.veterinary.repository.AppointmentRepository;
import com.Japkutija.veterinarybackend.veterinary.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {

        var appointment = appointmentMapper.toAppointment(appointmentDTO);

        return saveAppointment(appointment);
    }

    @Override
    public Appointment getAppointmentByUuid(UUID uuid) {

        var appointment = appointmentRepository.findByUuid(uuid);

        return appointment.orElseThrow(() -> new EntityNotFoundException(Appointment.class, uuid));
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception ex) {
            log.error("Error saving appointment: {}", ex.getMessage());
            throw new EntitySavingException(Appointment.class, ex);
        }
    }

    @Override
    public List<Appointment> getAllAppointments() {
        var appointments = appointmentRepository.findAll();

        if (appointments.isEmpty())
            return List.of();

        return appointments;
    }

    @Override
    public Appointment updateAppointment(UUID uuid, AppointmentDTO appointmentDTO) {

        var appointment = getAppointmentByUuid(uuid);
        appointmentMapper.updateAppointmentFromDto(appointmentDTO, appointment);

        return saveAppointment(appointment);
    }


    @Override
    public List<Appointment> getAppointmentsByOwner(UUID ownerUuid) {
        var appointments = appointmentRepository.findByOwnerUuid(ownerUuid);

        if (appointments.isEmpty())
            return List.of();

        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentsByPet(UUID petUuid) {
        var appointments = appointmentRepository.findByPetUuid(petUuid);

        if (appointments.isEmpty())
            return List.of();

        return appointments;
    }


    @Override
    public void deleteAppointment(UUID uuid) {

        var appointment = getAppointmentByUuid(uuid);
        appointmentRepository.delete(appointment);

    }
}
