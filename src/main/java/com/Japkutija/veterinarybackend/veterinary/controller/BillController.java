package com.Japkutija.veterinarybackend.veterinary.controller;

import com.Japkutija.veterinarybackend.veterinary.mapper.BillMapper;
import com.Japkutija.veterinarybackend.veterinary.model.dto.BillDTO;
import com.Japkutija.veterinarybackend.veterinary.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Bills", description = "Bills API")
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;
    private final BillMapper billMapper;

    @Operation(summary = "Create a new bill", description = "Creates a new bill and returns the created bill data", tags = {"bills"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bill created",
                    content = @Content(schema = @Schema(implementation = BillDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PostMapping
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillDTO billDTO) {

        var createdBill = billService.createBill(billDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(billMapper.toBillDto(createdBill));
    }

    @Operation(summary = "Get bill by uuid", description = "Returns the bill data for the given uuid", tags = {"bills"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill found",
                    content = @Content(schema = @Schema(implementation = BillDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bill not found")})
    @GetMapping("/{uuid}")
    public ResponseEntity<BillDTO> getBillByUuid(@PathVariable @NotNull UUID uuid) {

        var bill = billService.getBillByUuid(uuid);

        return ResponseEntity.ok(billMapper.toBillDto(bill));
    }

    @Operation(summary = "Update an existing bill", description = "Updates a bill and returns the updated bill data", tags = {"bills"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill updated",
                    content = @Content(schema = @Schema(implementation = BillDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bill not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PutMapping("/{uuid}")
    public ResponseEntity<BillDTO> updateBill(@PathVariable @NotNull UUID uuid, @RequestBody @Valid BillDTO billDTO) {

        var updatedBill = billService.updateBill(uuid, billDTO);

        return ResponseEntity.ok(billMapper.toBillDto(updatedBill));
    }

    @Operation(summary = "Delete a bill", description = "Deletes a bill by uuid", tags = {"bills"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bill deleted"),
            @ApiResponse(responseCode = "404", description = "Bill not found")})
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteBill(@PathVariable @NotNull UUID uuid) {

        billService.deleteBill(uuid);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all bills", description = "Returns a list of all bills", tags = {"bills"})
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = BillDTO[].class)))
    @GetMapping
    public ResponseEntity<List<BillDTO>> getAllBills() {

        var bills = billService.getAllBills();

        return ResponseEntity.ok(billMapper.toBillDTOList(bills));
    }

    @Operation(summary = "Get all bills by owner UUID", description = "Returns a list of all bills by owner UUID", tags = {"bills"})
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = BillDTO[].class)))
    @GetMapping("/owner/{ownerUuid}")
    public ResponseEntity<List<BillDTO>> getBillsByOwner(@PathVariable @NotNull UUID ownerUuid) {

        var bills = billService.getBillsByOwnerUuid(ownerUuid);

        return ResponseEntity.ok(billMapper.toBillDTOList(bills));
    }

    @Operation(summary = "Get bill by bill number", description = "Returns the bill data for the given bill number", tags = {"bills"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill found",
                    content = @Content(schema = @Schema(implementation = BillDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bill not found")})
    @GetMapping("/billNumber/{billNumber}")
    public ResponseEntity<BillDTO> getBillByBillNumber(@PathVariable @NotNull String billNumber) {

        var bill = billService.getBillByBillNumber(billNumber);

        return ResponseEntity.ok(billMapper.toBillDto(bill));
    }


}
