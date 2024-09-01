package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.AppointmentMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.AppointmentDTO;
import com.Japkutija.veterinarybackend.veterinary.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Appointments API")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;


    @Operation(summary = "Create a new appointment", description = "Creates a new appointment and returns the created appointment data", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {

        var createdAppointment = appointmentService.createAppointment(appointmentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentMapper.toAppointmentDto(createdAppointment));
    }

    @Operation(summary = "Update an existing appointment", description = "Updates an appointment and returns the updated appointment data", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment updated",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@NotNull UUID uuid, @RequestBody @Valid AppointmentDTO appointmentDTO) {
        var updatedAppointment = appointmentService.updateAppointment(uuid, appointmentDTO);

        return ResponseEntity.ok(appointmentMapper.toAppointmentDto(updatedAppointment));
    }

    @Operation(summary = "Get an appointment by UUID", description = "Returns a single appointment", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<AppointmentDTO> getAppointmentsByUuid(@PathVariable @NotNull UUID uuid) {
        var appointment = appointmentService.getAppointmentByUuid(uuid);

        return ResponseEntity.ok(appointmentMapper.toAppointmentDto(appointment));
    }

    @Operation(summary = "Get appointments by owner UUID", description = "Returns a list of appointments by owner UUID", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO[].class)))
    })
    @GetMapping("/owner/{ownerUuid}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByOwner(@PathVariable @NotNull UUID ownerUuid) {
        var appointments = appointmentService.getAppointmentsByOwner(ownerUuid);
        return ResponseEntity.ok(appointmentMapper.toAppointmentDTOList(appointments));
    }

    @Operation(summary = "Get appointments by pet UUID", description = "Returns a list of appointments by pet UUID", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO[].class)))
    })
    @GetMapping("/pet/{petUuid}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPet(@PathVariable @NotNull UUID petUuid) {
        var appointments = appointmentService.getAppointmentsByPet(petUuid);
        return ResponseEntity.ok(appointmentMapper.toAppointmentDTOList(appointments));
    }

    @Operation(summary = "List all appointments", description = "Returns a list of all appointments", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO[].class)))
    })
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        var appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointmentMapper.toAppointmentDTOList(appointments));
    }

    @Operation(summary = "Delete an appointment", description = "Deletes an appointment", tags = {"appointments"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment deleted"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID uuid) {
        appointmentService.deleteAppointment(uuid);
        return ResponseEntity.noContent().build();
    }


}
