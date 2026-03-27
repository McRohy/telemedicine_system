package sk.uniza.fri.telemedicine.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.TypeOfMeasurementRequest;
import sk.uniza.fri.telemedicine.dto.response.TypeOfMeasurementResponse;
import sk.uniza.fri.telemedicine.service.core.TypeOfMeasurementService;

import java.util.List;

/**
 * REST controller for measurement type management.
 * Provides endpoints for creating and viewing measurement types.
 */
@RestController
@RequestMapping("/api/measurement-types")
public class MeasurementTypeController {

    private final TypeOfMeasurementService typeOfMeasurementService;

    public MeasurementTypeController(TypeOfMeasurementService typeOfMeasurementService) {
        this.typeOfMeasurementService = typeOfMeasurementService;
    }

    @GetMapping("/select")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<TypeOfMeasurementResponse> getTypesOfMeasurement() {
        return typeOfMeasurementService.getTypesOfMeasurement();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<TypeOfMeasurementResponse> getPagedTypesOfMeasurement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTypeName) {
        return typeOfMeasurementService.getPagedTypesOfMeasurement(page, size, searchTypeName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TypeOfMeasurementResponse createTypeOfMeasurement(@Valid @RequestBody TypeOfMeasurementRequest request) {
        return typeOfMeasurementService.createTypeOfMeasurement(request);
    }
}
