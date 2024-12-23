package com.Japkutija.veterinarybackend.veterinary.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.Japkutija.veterinarybackend.veterinary.model.dto.AppointmentDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Appointment;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Bill;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentStatus;
import com.Japkutija.veterinarybackend.veterinary.model.enums.AppointmentType;
import com.Japkutija.veterinarybackend.veterinary.repository.AppointmentRepository;
import com.Japkutija.veterinarybackend.veterinary.service.BillService;
import com.Japkutija.veterinarybackend.veterinary.service.OwnerService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PetServiceImpl petService;

    @Mock
    private OwnerService ownerService;

    @Mock
    private BillService billService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    @DisplayName("Successfully schedule appointment when time slot is available and all data is valid")
    void scheduleAppointmentSuccess() {
        // Arrange
        var appointmentDTO = AppointmentDTO.builder()
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(Instant.now().plus(1, ChronoUnit.DAYS))
                .duration(30)
                .petUuid(UUID.randomUUID())
                .ownerUuid(UUID.randomUUID())
                .appointmentType(AppointmentType.GENERAL_CHECKUP)
                .reason("Regular checkup")
                .build();

        var mockPet = new Pet();
        var mockOwner = new Owner();
        var mockBill = new Bill();
        mockBill.setUuid(UUID.randomUUID());

        when(petService.getPetByUuid(appointmentDTO.getPetUuid())).thenReturn(mockPet);
        when(ownerService.getOwnerByUuid(appointmentDTO.getOwnerUuid())).thenReturn(mockOwner);
        when(appointmentRepository.findByAppointmentDate(any(LocalDate.class))).thenReturn(Optional.of(List.of()));
        when(billService.saveBill(any(Bill.class))).thenReturn(mockBill);
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        var result = appointmentService.scheduleAppointment(appointmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(AppointmentStatus.SCHEDULED, result.getStatus());
        assertEquals(appointmentDTO.getAppointmentDate(), result.getAppointmentDate());
        assertEquals(appointmentDTO.getAppointmentTime(), result.getAppointmentTime());
        verify(appointmentRepository).save(any(Appointment.class));
        verify(billService).saveBill(any(Bill.class));
    }

    @Test
    @DisplayName("Throw exception when time slot is not available")
    void scheduleAppointment_TimeSlotConflict_ThrowsAppointmentConflictException() {

        var appointmentDate = LocalDate.now().plusDays(1);
        var appointmentTime = Instant.now().plus(2, ChronoUnit.DAYS);

        var appointmentDTO = AppointmentDTO.builder()
                .appointmentDate(appointmentDate)
                .appointmentTime(appointmentTime)
                .duration(30)
                .petUuid(UUID.randomUUID())
                .ownerUuid(UUID.randomUUID())
                .appointmentType(AppointmentType.GENERAL_CHECKUP)
                .reason("Regular checkup")
                .build();

        var existingAppointment = Appointment.builder()
                .uuid(UUID.randomUUID())
                .appointmentDate(appointmentDate)
                .appointmentTime(appointmentTime.minus(10, ChronoUnit.MINUTES))
                .duration(30) // Overlaps
                .build();

        when(appointmentRepository.findByAppointmentDate(appointmentDate)).thenReturn(Optional.of(List.of(existingAppointment)));

        assertThrows(AppointmentConflictException.class, () -> appointmentService.scheduleAppointment(appointmentDTO));
        verify(appointmentRepository, never()).save(any(Appointment.class));
        verify(billService, never()).saveBill(any(Bill.class));
    }
}