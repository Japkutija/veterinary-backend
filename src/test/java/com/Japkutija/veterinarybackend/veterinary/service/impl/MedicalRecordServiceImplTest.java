package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
import com.Japkutija.veterinarybackend.veterinary.mapper.MedicalRecordMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Pet;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.MedicalRecordRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.PetRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.MedicalRecordService;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordServiceImpl;

    private UUID uuid;
    private MedicalRecord medicalRecord;
    private MedicalRecordDTO medicalRecordDTO;
    private Pet pet;
    private User veterinarian;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();

        pet = new Pet();
        pet.setUuid(UUID.randomUUID());

        veterinarian = new User();
        veterinarian.setUuid(UUID.randomUUID());
        veterinarian.setRole(Role.VETERINARIAN);

        medicalRecord = new MedicalRecord();
        medicalRecord.setUuid(uuid);
        medicalRecord.setPet(pet);
        medicalRecord.setVeterinarian(veterinarian);
        medicalRecord.setDiagnosis("Test diagnosis");
        medicalRecord.setTreatmentDiscipline("Test treatment");
        medicalRecord.setNotes("Test notes");
        medicalRecord.setVisitDate(new Date());

        medicalRecordDTO = new MedicalRecordDTO();
        medicalRecordDTO.setPetUuid(pet.getUuid());
        medicalRecordDTO.setVeterinarianUuid(veterinarian.getUuid());
        medicalRecordDTO.setDiagnosis("Test diagnosis");
        medicalRecordDTO.setTreatmentDiscipline("Test treatment");
        medicalRecordDTO.setNotes("Test notes");

    }

    @Test
    @DisplayName("Should create medical record successfully")
    void createMedicalRecord_Success() {
        // Arrange
        when(medicalRecordMapper.toMedicalRecord(any(MedicalRecordDTO.class))).thenReturn(medicalRecord);
        when(petRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(pet));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(veterinarian));
        when(applicationContext.getBean(MedicalRecordService.class)).thenReturn(medicalRecordService);
        when(medicalRecordService.saveMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);

        // Act
        var result = medicalRecordServiceImpl.createMedicalRecord(medicalRecordDTO);

        // Assert
        assertNotNull(result);
        assertEquals(medicalRecord.getDiagnosis(), result.getDiagnosis());
        assertEquals(medicalRecord.getTreatmentDiscipline(), result.getTreatmentDiscipline());
        verify(medicalRecordService).saveMedicalRecord(any(MedicalRecord.class));
    }


    @Test
    @DisplayName("Should throw EntityNotFoundException when pet not found during creation")
    void createMedicalRecord_PetNotFound() {
        // Arrange
        when(petRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> medicalRecordServiceImpl.createMedicalRecord(medicalRecordDTO));
        verify(medicalRecordService, never()).saveMedicalRecord(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when veterinarian not found during creation")
    void createMedicalRecord_VeterinarianNotFound() {
        // Arrange
        when(medicalRecordMapper.toMedicalRecord(any(MedicalRecordDTO.class))).thenReturn(medicalRecord);

        when(petRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(pet));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> medicalRecordServiceImpl.createMedicalRecord(medicalRecordDTO));
        verify(medicalRecordService, never()).saveMedicalRecord(any());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when non-veterinarian tries to create record")
    void createMedicalRecord_NonVeterinarian() {
        // Arrange
        veterinarian.setRole(Role.ADMIN);
        when(medicalRecordMapper.toMedicalRecord(any(MedicalRecordDTO.class))).thenReturn(medicalRecord);
        when(petRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(pet));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(veterinarian));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> medicalRecordServiceImpl.createMedicalRecord(medicalRecordDTO));
        verify(medicalRecordService, never()).saveMedicalRecord(any());
    }

    @Test
    @DisplayName("Should get medical record by UUID successfully")
    void getMedicalRecordByUuid_Success() {
        // Arrange
        when(medicalRecordRepository.findByUuid(uuid)).thenReturn(Optional.of(medicalRecord));

        // Act
        MedicalRecord result = medicalRecordServiceImpl.getMedicalRecordByUuid(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(medicalRecord.getUuid(), result.getUuid());
        assertEquals(medicalRecord.getDiagnosis(), result.getDiagnosis());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting non-existent medical record")
    void getMedicalRecordByUuid_NotFound() {
        // Arrange
        when(medicalRecordRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> medicalRecordServiceImpl.getMedicalRecordByUuid(uuid));
    }

    @Test
    @DisplayName("Should get all medical records by pet UUID successfully")
    void getMedicalRecordsByPetUuid_Success() {
        // Arrange
        when(medicalRecordRepository.findAllByPetUuid(pet.getUuid()))
                .thenReturn(Optional.of(Arrays.asList(medicalRecord)));

        // Act
        var results = medicalRecordServiceImpl.getMedicalRecordsByPetUuid(pet.getUuid());

        // Assert
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(medicalRecord.getUuid(), results.get(0).getUuid());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when no medical records found for pet")
    void getMedicalRecordsByPetUuid_NotFound() {
        // Arrange
        when(medicalRecordRepository.findAllByPetUuid(pet.getUuid())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> medicalRecordServiceImpl.getMedicalRecordsByPetUuid(pet.getUuid()));
    }

    @Test
    @DisplayName("Should update medical record successfully")
    void updateMedicalRecord_Success() {
        // Arrange
        when(medicalRecordRepository.findByUuid(uuid)).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordMapper.updateMedicalRecordFromDTO(any(), any())).thenReturn(medicalRecord);
        when(applicationContext.getBean(MedicalRecordService.class)).thenReturn(medicalRecordService);
        when(medicalRecordService.saveMedicalRecord(any(MedicalRecord.class))).thenReturn(medicalRecord);
        // Act
        var result = medicalRecordServiceImpl.updateMedicalRecord(medicalRecordDTO, uuid);

        // Assert
        assertNotNull(result);
        assertEquals(medicalRecord.getUuid(), result.getUuid());
        verify(medicalRecordService).saveMedicalRecord(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent medical record")
    void updateMedicalRecord_NotFound() {
        // Arrange
        when(medicalRecordRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> medicalRecordServiceImpl.updateMedicalRecord(medicalRecordDTO, uuid));
        verify(medicalRecordService, never()).saveMedicalRecord(any());
    }

    @Test
    @DisplayName("Should throw IllegalStateException when updating with non-veterinarian")
    void updateMedicalRecord_NonVeterinarian() {
        // Arrange
        veterinarian.setRole(Role.ADMIN);
        when(medicalRecordRepository.findByUuid(uuid)).thenReturn(Optional.of(medicalRecord));

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> medicalRecordServiceImpl.updateMedicalRecord(medicalRecordDTO, uuid));
        verify(medicalRecordService, never()).saveMedicalRecord(any());
    }

    @Test
    @DisplayName("Should delete medical record successfully")
    void deleteMedicalRecordByUuid_Success() {
        // Act
        medicalRecordServiceImpl.deleteMedicalRecordByUuid(uuid);

        // Assert
        verify(medicalRecordRepository).deleteByUuid(uuid);
    }

    @Test
    @DisplayName("Should throw GeneralRunTimeException when delete fails")
    void deleteMedicalRecordByUuid_Failure() {
        // Arrange
        doThrow(new RuntimeException("Delete failed")).when(medicalRecordRepository).deleteByUuid(uuid);

        // Act & Assert
        assertThrows(
                GeneralRunTimeException.class,
                () -> medicalRecordServiceImpl.deleteMedicalRecordByUuid(uuid));
    }
}
