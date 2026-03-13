package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.TypeOfMeasurementRequest;
import sk.uniza.fri.telemedicine.dto.response.TypeOfMeasurementResponse;
import sk.uniza.fri.telemedicine.services.core.TypeOfMeasurementService;

import java.util.List;

@RestController
@RequestMapping("/api/measurement-types")
public class TypeOfMeasurementController {

    private final TypeOfMeasurementService typeOfMeasurementService;

    public TypeOfMeasurementController(TypeOfMeasurementService typeOfMeasurementService) {
        this.typeOfMeasurementService = typeOfMeasurementService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public List<TypeOfMeasurementResponse> getAllTypesOfMeasurement() {
        return typeOfMeasurementService.getAllTypesOfMeasurement();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TypeOfMeasurementResponse updateTypeOfMeasurement(
            @PathVariable Integer id, @Valid @RequestBody TypeOfMeasurementRequest request
    ) {
        return typeOfMeasurementService.updateMinMaxTypeOfMeasurement(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TypeOfMeasurementResponse getTypeOfMeasurementById(@PathVariable Integer id) {
        return typeOfMeasurementService.findTypeOfMeasurementByIdResponse(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TypeOfMeasurementResponse createTypeOfMeasurement(@Valid @RequestBody TypeOfMeasurementRequest request) {
        return typeOfMeasurementService.createTypeOfMeasurement(request);
    }
}
