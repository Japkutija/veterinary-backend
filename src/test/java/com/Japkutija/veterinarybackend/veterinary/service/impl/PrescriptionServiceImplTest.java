package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
import com.Japkutija.veterinarybackend.veterinary.mapper.PrescriptionMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.MedicalRecordRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.PrescriptionRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PrescriptionMapper prescriptionMapper;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private UUID uuid;
    private Prescription prescription;
    private PrescriptionDTO prescriptionDTO;
    private MedicalRecord medicalRecord;
    private User veterinarian;
    private Pet pet;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();

        pet = new Pet();
        pet.setUuid(UUID.randomUUID());

        veterinarian = new User();
        veterinarian.setUuid(UUID.randomUUID());
        veterinarian.setRole(Role.VETERINARIAN);

        medicalRecord = new MedicalRecord();
        medicalRecord.setUuid(UUID.randomUUID());
        medicalRecord.setPet(pet);
        medicalRecord.setVeterinarian(veterinarian);

        prescription = new Prescription();
        prescription.setUuid(uuid);
        prescription.setMedicalRecord(medicalRecord);
        prescription.setVeterinarian(veterinarian);
        prescription.setMedication("Test medication");
        prescription.setDosage("Test dosage");
        prescription.setFrequency("Test frequency");
        prescription.setStartDate(LocalDate.now());
        prescription.setEndDate(LocalDate.now());
        prescription.setActive(true);

        prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setMedicalRecordUuid(medicalRecord.getUuid());
        prescriptionDTO.setVeterinarianUuid(veterinarian.getUuid());
        prescriptionDTO.setMedication("Test medication");
        prescriptionDTO.setDosage("Test dosage");
        prescriptionDTO.setFrequency("Test frequency");
        prescriptionDTO.setStartDate(LocalDate.now());
        prescriptionDTO.setEndDate(LocalDate.now());
        prescriptionDTO.setActive(true);
    }

    @Test
    @DisplayName("Should create prescription successfully")
    void createPrescription_Success() {
        // Arrange
        when(prescriptionMapper.toPrescription(any(PrescriptionDTO.class))).thenReturn(prescription);
        when(medicalRecordRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(medicalRecord));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(veterinarian));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        var result = prescriptionService.createPrescription(prescriptionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(prescription.getMedication(), result.getMedication());
        assertEquals(prescription.getDosage(), result.getDosage());
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when medical record not found during creation")
    void createPrescription_MedicalRecordNotFound() {
        // Arrange
        when(prescriptionMapper.toPrescription(any(PrescriptionDTO.class))).thenReturn(prescription);
        when(medicalRecordRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> prescriptionService.createPrescription(prescriptionDTO));
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when veterinarian not found during creation")
    void createPrescription_VeterinarianNotFound() {
        // Arrange
        when(prescriptionMapper.toPrescription(any(PrescriptionDTO.class))).thenReturn(prescription);
        when(medicalRecordRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(medicalRecord));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> prescriptionService.createPrescription(prescriptionDTO));
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when non-veterinarian tries to create prescription")
    void createPrescription_NonVeterinarian() {
        // Arrange
        veterinarian.setRole(Role.ADMIN);
        when(prescriptionMapper.toPrescription(any(PrescriptionDTO.class))).thenReturn(prescription);
        when(medicalRecordRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(medicalRecord));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(veterinarian));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> prescriptionService.createPrescription(prescriptionDTO));
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update prescription successfully without veterinarian change")
    void updatePrescription_SuccessWithoutVeterinarianChange() {
        // Arrange
        prescriptionDTO.setVeterinarianUuid(null);
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.of(prescription));
        when(prescriptionMapper.updatePrescriptionFromDTO(prescriptionDTO, prescription)).thenReturn(prescription);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        var result = prescriptionService.updatePrescription(prescriptionDTO, uuid);

        // Assert
        assertNotNull(result);
        assertEquals(prescription.getMedication(), result.getMedication());
        verify(prescriptionRepository).save(any(Prescription.class));
        verify(userRepository, never()).findByUuid(any());
    }

    @Test
    @DisplayName("Should update prescription successfully with veterinarian change")
    void updatePrescription_SuccessWithVeterinarianChange() {
        // Arrange
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.of(prescription));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(veterinarian));
        when(prescriptionMapper.updatePrescriptionFromDTO(any(), any())).thenReturn(prescription);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        var result = prescriptionService.updatePrescription(prescriptionDTO, uuid);

        // Assert
        assertNotNull(result);
        assertEquals(prescription.getMedication(), result.getMedication());
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent prescription")
    void updatePrescription_NotFound() {
        // Arrange
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> prescriptionService.updatePrescription(prescriptionDTO, uuid));
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when updating with non-veterinarian")
    void updatePrescription_NonVeterinarian() {
        // Arrange
        veterinarian.setRole(Role.ADMIN);
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.of(prescription));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(veterinarian));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> prescriptionService.updatePrescription(prescriptionDTO, uuid));
        verify(prescriptionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should deactivate prescription successfully")
    void deactivatePrescription_Success() {
        // Arrange
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

        // Act
        var result = prescriptionService.deactivatePrescription(uuid);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive());
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deactivating non-existent prescription")
    void deactivatePrescription_NotFound() {
        // Arrange
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> prescriptionService.deactivatePrescription(uuid));
    }

    @Test
    @DisplayName("Should get prescription by UUID successfully")
    void getPrescriptionByUuid_Success() {
        // Arrange
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.of(prescription));

        // Act
        var result = prescriptionService.getPrescriptionByUuid(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(prescription.getUuid(), result.getUuid());
        assertEquals(prescription.getMedication(), result.getMedication());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting non-existent prescription")
    void getPrescriptionByUuid_NotFound() {
        // Arrange
        when(prescriptionRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> prescriptionService.getPrescriptionByUuid(uuid));
    }

    @Test
    @DisplayName("Should get prescriptions by medical record UUID successfully")
    void getPrescriptionsByMedicalRecordUuid_Success() {
        // Arrange
        when(prescriptionRepository.findByMedicalRecordUuid(medicalRecord.getUuid()))
                .thenReturn(Optional.of(Arrays.asList(prescription)));

        // Act
        var results = prescriptionService.getPrescriptionsByMedicalRecordUuid(medicalRecord.getUuid());

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(prescription.getUuid(), results.get(0).getUuid());
    }

    @Test
    @DisplayName("Should return empty list when no prescriptions found for medical record")
    void getPrescriptionsByMedicalRecordUuid_NotFound() {
        // Arrange
        when(prescriptionRepository.findByMedicalRecordUuid(medicalRecord.getUuid()))
                .thenReturn(Optional.empty());

        // Act
        var results = prescriptionService.getPrescriptionsByMedicalRecordUuid(medicalRecord.getUuid());

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should get active prescriptions by pet UUID successfully")
    void getActivePrescriptionsByPetUuid_Success() {
        // Arrange
        when(prescriptionRepository.findByMedicalRecord_Pet_UuidAndIsActiveIsTrue(pet.getUuid()))
                .thenReturn(Optional.of(Arrays.asList(prescription)));

        // Act
        var results = prescriptionService.getActivePrescriptionsByPetUuid(pet.getUuid());

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(prescription.getUuid(), results.get(0).getUuid());
        assertTrue(results.get(0).isActive());
    }

    @Test
    @DisplayName("Should return empty list when no active prescriptions found for pet")
    void getActivePrescriptionsByPetUuid_NotFound() {
        // Arrange
        when(prescriptionRepository.findByMedicalRecord_Pet_UuidAndIsActiveIsTrue(pet.getUuid()))
                .thenReturn(Optional.empty());

        // Act
        var results = prescriptionService.getActivePrescriptionsByPetUuid(pet.getUuid());

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should delete prescription successfully")
    void deletePrescription_Success() {
        // Act
        prescriptionService.deletePrescription(uuid);

        // Assert
        verify(prescriptionRepository).deleteByUuid(uuid);
    }

    @Test
    @DisplayName("Should throw GeneralRunTimeException when delete fails")
    void deletePrescription_Failure() {
        // Arrange
        doThrow(new RuntimeException("Delete failed")).when(prescriptionRepository).deleteByUuid(uuid);

        // Act & Assert
        assertThrows(GeneralRunTimeException.class,
                () -> prescriptionService.deletePrescription(uuid));
    }
}