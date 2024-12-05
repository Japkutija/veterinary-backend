package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.MedicalRecordMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.MedicalRecordDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.MedicalRecord;
import com.Japkutija.veterinarybackend.veterinary.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/medical-records")
@Tag(name = "Medical Records Management", description = "API for managing veterinary medical records")
@RequiredArgsConstructor
@Validated
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordMapper medicalRecordMapper;

    @PostMapping
    @PreAuthorize("hasRole('VETERINARIAN')")
    @Operation(summary = "Create new medical record", 
               description = "Creates a new medical record. Only available to veterinarians.", 
               tags = {"medical-records"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medical record created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN role")
    })
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        var medicalRecord = medicalRecordService.createMedicalRecord(medicalRecordDTO);
        return new ResponseEntity<>(medicalRecordMapper.toMedicalRecordDTO(medicalRecord), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get medical record by UUID", 
               description = "Retrieves a specific medical record by its UUID", 
               tags = {"medical-records"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical record found"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable UUID uuid) {
        var medicalRecord = medicalRecordService.getMedicalRecordByUuid(uuid);
        return ResponseEntity.ok(medicalRecordMapper.toMedicalRecordDTO(medicalRecord));
    }

    @GetMapping("/pet/{petUuid}")
    @Operation(summary = "Get medical records by pet UUID", 
               description = "Retrieves all medical records for a specific pet", 
               tags = {"medical-records"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByPet(@PathVariable UUID petUuid) {
        var medicalRecords = medicalRecordService.getMedicalRecordsByPetUuid(petUuid);
        return ResponseEntity.ok(medicalRecordMapper.toMedicalRecordDTOList(medicalRecords));
    }

    @GetMapping
    @PreAuthorize("hasRole('VETERINARIAN') or hasRole('ADMIN')")
    @Operation(summary = "Get all medical records", 
               description = "Retrieves all medical records. Only available to veterinarians and admins.", 
               tags = {"medical-records"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical records retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN or ADMIN role")
    })
    public ResponseEntity<List<MedicalRecordDTO>> getAllMedicalRecords() {
        var medicalRecords = medicalRecordService.getAllMedicalRecords();
        return ResponseEntity.ok(medicalRecordMapper.toMedicalRecordDTOList(medicalRecords));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('VETERINARIAN')")
    @Operation(summary = "Update medical record", 
               description = "Updates an existing medical record. Only available to veterinarians.", 
               tags = {"medical-records"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medical record updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN role"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable UUID uuid,
            @Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        var updatedRecord = medicalRecordService.updateMedicalRecord(medicalRecordDTO, uuid);
        return ResponseEntity.ok(medicalRecordMapper.toMedicalRecordDTO(updatedRecord));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('VETERINARIAN') or hasRole('ADMIN')")
    @Operation(summary = "Delete medical record", 
               description = "Deletes a medical record. Only available to veterinarians and admins.", 
               tags = {"medical-records"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medical record deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN or ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable UUID uuid) {
        medicalRecordService.deleteMedicalRecordByUuid(uuid);
        return ResponseEntity.noContent().build();
    }
}
