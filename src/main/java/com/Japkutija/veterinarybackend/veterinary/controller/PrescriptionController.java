package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.PrescriptionMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.PrescriptionDTO;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Prescription;
import com.Japkutija.veterinarybackend.veterinary.service.PrescriptionService;
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
@RequestMapping("/api/v1/prescriptions")
@Tag(name = "Prescription Management", description = "API for managing veterinary prescriptions")
@RequiredArgsConstructor
@Validated
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PrescriptionMapper prescriptionMapper;

    @PostMapping
    @PreAuthorize("hasRole('VETERINARIAN')")
    @Operation(summary = "Create new prescription", 
               description = "Creates a new prescription for a medical record. Only available to veterinarians.", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Prescription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN role"),
            @ApiResponse(responseCode = "404", description = "Medical record or veterinarian not found")
    })
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        var prescription = prescriptionService.createPrescription(prescriptionDTO);
        return new ResponseEntity<>(prescriptionMapper.toPrescriptionDTO(prescription), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get prescription by UUID", 
               description = "Retrieves a specific prescription by its UUID", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription found"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    public ResponseEntity<PrescriptionDTO> getPrescription(@PathVariable UUID uuid) {
        var prescription = prescriptionService.getPrescriptionByUuid(uuid);
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(prescription));
    }

    @GetMapping("/medical-record/{medicalRecordUuid}")
    @Operation(summary = "Get prescriptions by medical record", 
               description = "Retrieves all prescriptions associated with a specific medical record", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescriptions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptionsByMedicalRecord(@PathVariable UUID medicalRecordUuid) {
        var prescriptions = prescriptionService.getPrescriptionsByMedicalRecordUuid(medicalRecordUuid);
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTOList(prescriptions));
    }

    @GetMapping("/pet/{petUuid}/active")
    @Operation(summary = "Get active prescriptions by pet", 
               description = "Retrieves all active prescriptions for a specific pet", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active prescriptions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    public ResponseEntity<List<PrescriptionDTO>> getActivePrescriptionsByPet(@PathVariable UUID petUuid) {
        var prescriptions = prescriptionService.getActivePrescriptionsByPetUuid(petUuid);
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTOList(prescriptions));
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasRole('VETERINARIAN')")
    @Operation(summary = "Update prescription", 
               description = "Updates an existing prescription. Only available to veterinarians.", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN role"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    public ResponseEntity<PrescriptionDTO> updatePrescription(
            @PathVariable UUID uuid,
            @Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        var updatedPrescription = prescriptionService.updatePrescription(prescriptionDTO, uuid);
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(updatedPrescription));
    }

    @PatchMapping("/{uuid}/deactivate")
    @PreAuthorize("hasRole('VETERINARIAN')")
    @Operation(summary = "Deactivate prescription", 
               description = "Deactivates an existing prescription. Only available to veterinarians.", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription deactivated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN role"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    public ResponseEntity<PrescriptionDTO> deactivatePrescription(@PathVariable UUID uuid) {
        var deactivatedPrescription = prescriptionService.deactivatePrescription(uuid);
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(deactivatedPrescription));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasRole('VETERINARIAN') or hasRole('ADMIN')")
    @Operation(summary = "Delete prescription", 
               description = "Deletes a prescription. Only available to veterinarians and admins.", 
               tags = {"prescriptions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - requires VETERINARIAN or ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    public ResponseEntity<Void> deletePrescription(@PathVariable UUID uuid) {
        prescriptionService.deletePrescription(uuid);
        return ResponseEntity.noContent().build();
    }
}
