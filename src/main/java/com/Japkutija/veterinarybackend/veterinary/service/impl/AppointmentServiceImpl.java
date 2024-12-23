package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.mapper.AppointmentMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.AppointmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Appointment;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;
import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentStatus;
import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentType;
import com.Japkutija.veterinarybackend.veterinary.model.enums.BillStatus;
import com.Japkutija.veterinarybackend.veterinary.repository.AppointmentRepository;
import com.Japkutija.veterinarybackend.veterinary.service.AppointmentService;
import com.Japkutija.veterinarybackend.veterinary.service.BillService;
import com.Japkutija.veterinarybackend.veterinary.service.OwnerService;
import com.Japkutija.veterinarybackend.veterinary.service.PetService;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PetService petService;
    private final OwnerService ownerService;
    private final BillService billService;

    @Override
    @Transactional
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        var appointment = appointmentMapper.toAppointment(appointmentDTO);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    @Override
    public Appointment scheduleAppointment(AppointmentDTO appointmentDTO) {
        // Check for time slot conflicts
        if (!isTimeSlotAvailable(appointmentDTO.getAppointmentDate(), appointmentDTO.getAppointmentTime(), appointmentDTO.getDuration())) {
            throw new AppointmentConflictException("Appointment time slot is not available");
        }

        var pet = petService.getPetByUuid(appointmentDTO.getPetUuid());
        var owner = ownerService.getOwnerByUuid(appointmentDTO.getOwnerUuid());

        // Create the appointment
        var appointment = Appointment.builder()
                .uuid(UUID.randomUUID())
                .appointmentDate(appointmentDTO.getAppointmentDate())
                .appointmentTime(appointmentDTO.getAppointmentTime())
                .duration(appointmentDTO.getDuration())
                .appointmentType(appointmentDTO.getAppointmentType())
                .reason(appointmentDTO.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .pet(pet)
                .owner(owner)
                .build();

        // Create a new bill for the appointment
        var bill = new Bill();
        bill.setUuid(UUID.randomUUID());
        bill.setTotalAmount(calculateTotalAmount(appointmentDTO.getAppointmentType()));
        bill.setDateOfIssue(LocalDate.now());
        bill.setStatus(BillStatus.PENDING);

        // Save the bill and associate it with the appointment
        bill = billService.saveBill(bill);
        appointment.setBill(bill);

        return appointmentRepository.save(appointment);
    }

    private BigDecimal calculateTotalAmount(AppointmentType appointmentType) {
        return switch (appointmentType) {
            case GENERAL_CHECKUP -> BigDecimal.valueOf(30.00);
            case VACCINATION -> BigDecimal.valueOf(15.00);
            case COMPLEX_EXAMINATION -> BigDecimal.valueOf(45.00);
            default -> BigDecimal.ZERO;
        };
    }

    public boolean isTimeSlotAvailable(LocalDate appointmentDate, Instant appointmentTime, int duration) {
        // Calculate the end time of the appointment
        var endTime = appointmentTime.plus(Duration.ofMinutes(duration));

        // Check for existing appointments on the same date
        var existingAppointments = appointmentRepository.findByAppointmentDate(appointmentDate).orElse(List.of());

        var hasConflict = existingAppointments.stream()
                .anyMatch(appointment -> {
                    var existingStart = appointment.getAppointmentTime();
                    var existingEnd = existingStart.plus(Duration.ofMinutes(appointment.getDuration()));

                        /*
                        Overlap occurs if the new appointment starts before an existing one ends,
                         and the new appointment ends after the existing one starts.
                         */
                    return appointmentTime.isBefore(existingEnd) && endTime.isAfter(existingStart);
                });

        return !hasConflict;
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment getAppointmentByUuid(UUID uuid) {

        var appointment = appointmentRepository.findByUuid(uuid);

        return appointment.orElseThrow(() -> new EntityNotFoundException(Appointment.class, uuid));
    }

    @Override
    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
        try {
            return appointmentRepository.save(appointment);
        } catch (Exception ex) {
            log.error("Error saving appointment: {}", ex.getMessage());
            throw new EntitySavingException(Appointment.class, ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        var appointments = appointmentRepository.findAll();

        if (appointments.isEmpty()) {
            return List.of();
        }

        return appointments;
    }

    @Override
    @Transactional
    public Appointment updateAppointment(UUID uuid, AppointmentDTO appointmentDTO) {

        var appointment = getAppointmentByUuid(uuid);
        var updatedAppointment = appointmentMapper.updateAppointmentFromDto(appointmentDTO, appointment);

        return saveAppointment(updatedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByOwner(UUID ownerUuid) {
        var appointments = appointmentRepository.findByOwnerUuid(ownerUuid);

        if (appointments.isEmpty()) {
            return List.of();
        }

        return appointments;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPet(UUID petUuid) {
        var appointments = appointmentRepository.findByPetUuid(petUuid);

        if (appointments.isEmpty()) {
            return List.of();
        }

        return appointments;
    }

    @Override
    @Transactional
    public void deleteAppointment(UUID uuid) {

        var appointment = getAppointmentByUuid(uuid);
        appointmentRepository.delete(appointment);
    }
}
